package com.cn.langujet.domain.correction.service

import com.cn.langujet.actor.correction.payload.request.AssignCorrectionRequest
import com.cn.langujet.actor.correction.payload.request.AssignCorrectionToCorrectorRequest
import com.cn.langujet.actor.correction.payload.response.*
import com.cn.langujet.actor.util.Auth
import com.cn.langujet.application.advice.UnprocessableException
import com.cn.langujet.application.service.file.domain.service.FileService
import com.cn.langujet.domain.answer.AnswerRepository
import com.cn.langujet.domain.answer.model.Answer
import com.cn.langujet.domain.correction.model.CorrectionEntity
import com.cn.langujet.domain.correction.model.CorrectionStatus
import com.cn.langujet.domain.correction.model.CorrectorType
import com.cn.langujet.domain.correction.repository.CorrectAnswerRepository
import com.cn.langujet.domain.correction.repository.CorrectionRepository
import com.cn.langujet.domain.correction.service.corrector.AutoCorrectorService
import com.cn.langujet.domain.corrector.CorrectorService
import com.cn.langujet.domain.exam.model.ExamSession
import com.cn.langujet.domain.exam.model.SpeakingPart
import com.cn.langujet.domain.exam.model.WritingPart
import com.cn.langujet.domain.exam.repository.ExamSessionRepository
import com.cn.langujet.domain.exam.service.ExamSectionContentService
import com.cn.langujet.domain.exam.service.SectionService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrNull

@Service
class CorrectionService(
    private val correctionRepository: CorrectionRepository,
    private val correctAnswerRepository: CorrectAnswerRepository,
    private val autoCorrectorService: AutoCorrectorService,
    private val sectionService: SectionService,
    private val correctorService: CorrectorService,
    private val answerRepository: AnswerRepository,
    private val examSessionRepository: ExamSessionRepository,
    private val examSectionContentService: ExamSectionContentService,
    private val fileService: FileService,
) {
    private val logger = LoggerFactory.getLogger(this::class.java)
    
    fun makeExamSessionCorrection(examSession: ExamSession) {
        val sections = sectionService.getSectionsMetaData(examSession.examId)
        val corrections = sections.map { section ->
            val correctorType = if (correctAnswerRepository.existsByExamIdAndSectionOrder(examSession.examId, section.order)) {
                CorrectorType.AUTO_CORRECTION
            } else {
                examSession.correctorType
            }
            CorrectionEntity(
                correctorType,
                CorrectionStatus.PENDING,
                examSession.id ?: "",
                examSession.examType,
                section.order,
                section.sectionType
            )
        }
        correctionRepository.saveAll(corrections).forEach {
            if (it.correctorType == CorrectorType.AUTO_CORRECTION)
                autoCorrectorService.correctExamSection(examSession, it)
        }
    }
    
    fun changeStatus(correctionEntity: CorrectionEntity, status: CorrectionStatus) {
        correctionEntity.status = status
        correctionEntity.refreshUpdatedDate()
        correctionRepository.save(correctionEntity)
    }
    
    fun getCorrectionsByExamSessionId(examSessionId: String): List<CorrectionEntity> {
        return correctionRepository.findAllByExamSessionId(examSessionId)
    }
    
    fun areAllSectionCorrectionProcessed(examSessionId: String): Boolean {
        getCorrectionsByExamSessionId(examSessionId).forEach {
            if (it.status != CorrectionStatus.PROCESSED) return false
        }
        return true
    }
    
    private fun getCorrectorPendingCorrectionsPerExamSessionId(): Map<String, List<CorrectionEntity>> {
        return correctionRepository.findByCorrectorTypeAndStatusOrderByCreatedDateAsc(
            CorrectorType.HUMAN, CorrectionStatus.PENDING
        ).groupBy {
            it.examSessionId
        }
    }
    
    fun getCorrectorPendingCorrections(): List<CorrectorAvailableCorrectionResponse> {
        val responses = mutableListOf<CorrectorAvailableCorrectionResponse>()
        getCorrectorPendingCorrectionsPerExamSessionId().forEach { (examSessionId, corrections) ->
            val sectionTypes = corrections.map { it.sectionType }
            val response = responses.find {
                it.examType == corrections[0].examType &&
                    it.sectionTypes == sectionTypes
            }?.also {
                it.count++
            }
            if (response == null) {
                responses += CorrectorAvailableCorrectionResponse(
                    corrections[0].examType,
                    sectionTypes,
                    1
                )
            }
        }
        return responses
    }
    
    fun assignCorrection(
        assignCorrectionRequest: AssignCorrectionRequest,
        correctorUserId: String = Auth.userId()
    ): CorrectionResponse {
        if (getCorrectorProcessingCorrectionByUserId(correctorUserId).isNotEmpty())
            throw UnprocessableException("Finish in-progress exam correction first")
        return try {
            val foundedCorrections = getCorrectorPendingCorrectionsPerExamSessionId().filter {
                val sectionTypesCondition = it.value.map {
                    correction -> correction.sectionType
                }.sorted() == assignCorrectionRequest.sectionTypes.sorted()
                val examTypeCondition = it.value.first().examType == assignCorrectionRequest.examType
                sectionTypesCondition && examTypeCondition
            }.firstNotNullOf {
                it.value
            }
            correctionRepository.saveAll(
                foundedCorrections.onEach {
                    it.correctorUserId = correctorUserId
                    it.status = CorrectionStatus.PROCESSING
                }
            )
            CorrectionResponse(
                foundedCorrections.first().examType,
                foundedCorrections.map {
                    correction -> CorrectionSectionResponse(
                    correction.id, correction.sectionType, correction.sectionOrder
                    )
                }
            )
        } catch (ex: NoSuchElementException) {
            throw UnprocessableException("There is no exam of this type left for correction")
        }
    }
    
    private fun getCorrectorProcessingCorrectionByUserId(correctorUserId: String): List<CorrectionEntity> {
        return correctionRepository.findByStatusAndCorrectorUserId(CorrectionStatus.PROCESSING, correctorUserId)
    }
    
    fun getCorrectorProcessingCorrection(): CorrectionResponse {
        val foundedCorrections = getCorrectorProcessingCorrectionByUserId(Auth.userId())
        if (foundedCorrections.isNotEmpty()) {
            return CorrectionResponse(
                foundedCorrections.first().examType,
                foundedCorrections.map {
                    correction -> CorrectionSectionResponse(
                    correction.id, correction.sectionType, correction.sectionOrder
                    )
                }
            )
        } else {
            throw UnprocessableException("You don't have any exam to correct")
        }
    }
    
    fun assignCorrectionToCorrector(
        assignCorrectionToCorrectorRequest: AssignCorrectionToCorrectorRequest
    ): CorrectionResponse {
        if (!correctorService.correctorExistsByUserId(assignCorrectionToCorrectorRequest.correctorUserId))
            throw UnprocessableException("The user isn't a Corrector")
        return assignCorrection(
            AssignCorrectionRequest(
                assignCorrectionToCorrectorRequest.examType,
                assignCorrectionToCorrectorRequest.sectionTypes
            ),
            assignCorrectionToCorrectorRequest.correctorUserId
        )
    }
    
    fun getCorrectorCorrectionExamSessionContent(correctionId: String): CorrectorCorrectionExamSessionContentResponse {
        val correction = getCorrectorCorrectionEntity(correctionId)
        
        val answers = answerRepository.findAllByExamSessionIdAndSectionOrder(
            correction.examSessionId, correction.sectionOrder
        )
        val examSession = examSessionRepository.findById(correction.examSessionId).getOrNull()
        val section = sectionService.getSectionByExamIdAndOrder(examSession?.examId ?: "", correction.sectionOrder)
        return CorrectorCorrectionExamSessionContentResponse(
            examSessionId = correction.examSessionId,
            section = CorrectorCorrectionSectionResponse(
                header = section.header,
                sectionOrder = section.order,
                sectionType = section.sectionType,
                parts = section.parts.mapNotNull { part ->
                    when(part) {
                        is WritingPart -> WritingCorrectorCorrectionPartResponse(
                            partOrder = part.order,
                            question = WritingCorrectorCorrectionQuestionResponse(
                                header = part.question.header,
                                content = part.question.content?.let { content ->
                                    examSectionContentService.replaceFileIdsWithDownloadLink(content)
                                }
                            ),
                            answer = answers.filterIsInstance<Answer.TextAnswer>().find { answer ->
                                (answer.partOrder == part.order) && (answer.questionOrder == part.question.order)
                            }?.let { existingAnswer ->
                                TextCorrectorCorrectionAnswerResponse(
                                    text = existingAnswer.text
                                )
                            }
                        )
                        
                        is SpeakingPart -> SpeakingCorrectorCorrectionPartResponse(
                            partOrder = part.order,
                            questionAnswerList = part.questionList.map { question ->
                                SpeakingCorrectorCorrectionQuestionAnswerResponse(
                                    questionOrder = question.order,
                                    question = SpeakingCorrectorCorrectionQuestionResponse(
                                        header = question.header,
                                        audioId = question.audioId?.let { audioId ->
                                            try {
                                                fileService.generatePublicDownloadLink(
                                                    audioId, (24 * 3600)
                                                )
                                            } catch (ex: Exception) {
                                                logger.error("There is an invalid FileId in sectionId = ${section.id} , partOrder = ${part.order} , questionOrder = ${question.order} , audioId = $audioId  ")
                                                null
                                            }
                                        }
                                    ),
                                    answer = answers.filterIsInstance<Answer.VoiceAnswer>().find { answer ->
                                        (answer.partOrder == part.order) && (answer.questionOrder == question.order)
                                    }?.let { existingAnswer ->
                                        VoiceCorrectorCorrectionAnswerResponse(
                                            voiceLink = fileService.generatePublicDownloadLink(
                                                existingAnswer.voiceFileId, (24 * 3600)
                                            )
                                        )
                                    }
                                )
                            }
                        )
                        
                        else -> null
                    }
                }
            )
        )
    }
    
    fun getCorrectorCorrectionEntity(correctionId: String): CorrectionEntity {
        val correction = correctionRepository.findById(correctionId).orElseThrow {
            throw UnprocessableException("Exam correction not found")
        }
        if (correction.correctorUserId != Auth.userId())
            throw UnprocessableException("You don't have access to this correction")
        if (correction.status == CorrectionStatus.PROCESSED)
            throw UnprocessableException("You have already corrected this part")
        return correction
    }
}
