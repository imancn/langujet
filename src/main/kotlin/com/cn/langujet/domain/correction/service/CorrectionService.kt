package com.cn.langujet.domain.correction.service

import com.cn.langujet.actor.correction.payload.request.AssignCorrectionRequest
import com.cn.langujet.actor.correction.payload.request.AssignCorrectionToCorrectorRequest
import com.cn.langujet.actor.correction.payload.request.AssignSpecificCorrectionToCorrectorRequest
import com.cn.langujet.actor.correction.payload.response.*
import com.cn.langujet.actor.util.Auth
import com.cn.langujet.application.arch.advice.UnprocessableException
import com.cn.langujet.application.arch.models.entity.Entity
import com.cn.langujet.application.service.file.domain.service.FileService
import com.cn.langujet.domain.answer.AnswerRepository
import com.cn.langujet.domain.answer.model.AnswerEntity
import com.cn.langujet.domain.correction.model.CorrectionStatus
import com.cn.langujet.domain.correction.model.CorrectorType
import com.cn.langujet.domain.correction.repository.CorrectAnswerRepository
import com.cn.langujet.domain.correction.service.corrector.auto.AutoCorrectorService
import com.cn.langujet.domain.corrector.CorrectorService
import com.cn.langujet.domain.exam.model.ExamSessionEntity
import com.cn.langujet.domain.exam.model.section.part.SpeakingPartEntity
import com.cn.langujet.domain.exam.model.section.part.WritingPartEntity
import com.cn.langujet.domain.exam.model.section.part.questions.SpeakingQuestionEntity
import com.cn.langujet.domain.exam.model.section.part.questions.WritingQuestionEntity
import com.cn.langujet.domain.exam.service.*
import com.cn.langujet.domain.result.model.ResultEntity
import com.cn.langujet.domain.result.service.ResultService
import com.cn.langujet.domain.result.service.SectionResultService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.stereotype.Service

@Service
class CorrectionService(
    private val correctAnswerRepository: CorrectAnswerRepository,
    private val autoCorrectorService: AutoCorrectorService,
    private val sectionService: SectionService,
    private val correctorService: CorrectorService,
    private val answerRepository: AnswerRepository,
    private val examContentService: ExamContentService,
    private val fileService: FileService,
    private val resultService: ResultService,
    private val sectionResultService: SectionResultService,
) {
    @Autowired
    private lateinit var questionService: QuestionService
    
    @Autowired
    private lateinit var partService: PartService
    
    @Autowired @Lazy
    private lateinit var examSessionService: ExamSessionService
    
    private val logger = LoggerFactory.getLogger(this::class.java)
    
    fun initiateExamSessionCorrection(examSession: ExamSessionEntity) {
        val sections = sectionService.getSectionsMetaData(examSession.examId)
        val result = resultService.initiateResult(examSession)
        val sectionResults = sections.map { section ->
            val isAutoCorrection = correctAnswerRepository.existsByExamIdAndSectionOrder(examSession.examId, section.order)
            if (isAutoCorrection) {
                autoCorrectorService.correctExamSection(
                    examSession, result.id ?: Entity.UNKNOWN_ID,
                    section.order,
                    section.sectionType
                ) ?: sectionResultService.initSectionResult(result.id ?: Entity.UNKNOWN_ID, examSession, section)
            } else {
                sectionResultService.initSectionResult(result.id ?: Entity.UNKNOWN_ID, examSession, section)
            }
        }
        if (resultService.areAllSectionResultsApproved(result)) {
            resultService.finalizeCorrection(sectionResults, result)
        }
    }
    
    fun getCorrectorPendingCorrections(correctorType: CorrectorType): List<CorrectorAvailableCorrectionResponse> {
        val responses = mutableListOf<CorrectorAvailableCorrectionResponse>()
        resultService.getResultsByStatusAndCorrectorType(CorrectionStatus.PENDING, correctorType).forEach { result ->
            val response = responses.find {
                it.examType == result.examType && it.examMode == result.examMode
            }?.also {
                it.count++
            }
            if (response == null) {
                responses += CorrectorAvailableCorrectionResponse(
                    result.examType, result.examMode, 1
                )
            }
        }
        return responses
    }
    
    fun assignCorrectionByCorrector(
        assignCorrectionRequest: AssignCorrectionRequest,
        correctorType: CorrectorType,
        correctorUserId: Long = Auth.userId()
    ): CorrectionResponse {
        if (resultService.getCorrectorResultsByStatus(CorrectionStatus.PROCESSING, correctorUserId).isNotEmpty()) {
            throw UnprocessableException("Finish in-progress exam correction first")
        }
        return try {
            val pendingResult = resultService.getResultsByStatusAndCorrectorType(CorrectionStatus.PENDING, correctorType).first {
                it.examMode == assignCorrectionRequest.examMode && it.examType == assignCorrectionRequest.examType
            }
            assignCorrection(pendingResult, correctorUserId)
        } catch (ex: NoSuchElementException) {
            throw UnprocessableException("There is no exam of this type left for correction")
        }
    }
    
    fun assignCorrectionToCorrector(
        assignCorrectionToCorrectorRequest: AssignCorrectionToCorrectorRequest
    ): CorrectionResponse {
        if (!correctorService.correctorExistsByUserId(assignCorrectionToCorrectorRequest.correctorUserId)) {
            throw UnprocessableException("The user isn't a Corrector")
        }
        return assignCorrectionByCorrector(
            AssignCorrectionRequest(
                assignCorrectionToCorrectorRequest.examType, assignCorrectionToCorrectorRequest.examMode
            ),
            CorrectorType.HUMAN,
            assignCorrectionToCorrectorRequest.correctorUserId
        )
    }
    
    fun assignSpecificCorrectionToCorrector(
        request: AssignSpecificCorrectionToCorrectorRequest
    ): CorrectionResponse {
        val result = resultService.getResultByExamSessionId(request.examSessionId)
            .orElseThrow { UnprocessableException("Correction not found") }
        if (result.status != CorrectionStatus.PENDING) {
            throw UnprocessableException("This exam correction had been assigned to another corrector")
        }
        if (resultService.getCorrectorResultsByStatus(CorrectionStatus.PROCESSING, request.correctorUserId).isNotEmpty()) {
            throw UnprocessableException("Corrector must finish in-progress exam correction first")
        }
        return assignCorrection(result, request.correctorUserId)
    }
    
    private fun assignCorrection(
        pendingResult: ResultEntity, correctorUserId: Long
    ): CorrectionResponse {
        val sectionCorrections = sectionResultService.getByStatusAndResultId(
            CorrectionStatus.PENDING, pendingResult.id ?: Entity.UNKNOWN_ID
        )
        resultService.assignResultToCorrector(pendingResult, correctorUserId)
        sectionResultService.assignSectionResultToCorrector(sectionCorrections, correctorUserId)
        return CorrectionResponse(
            pendingResult.id ?: Entity.UNKNOWN_ID,
            pendingResult.examType,
            pendingResult.examMode,
            sectionCorrections.map { correction ->
                CorrectionSectionResponse(
                    correction.id, correction.sectionType, correction.sectionOrder
                )
            })
    }
    
    fun getCorrectorProcessingCorrection(): CorrectionResponse {
        val result = resultService.getCorrectorResultsByStatus(
            CorrectionStatus.PROCESSING, Auth.userId()
        ).firstOrNull()
        if (result != null) {
            val sectionResults = sectionResultService.getByResultId(
                result.id ?: Entity.UNKNOWN_ID
            ).filter { it.correctorUserId == Auth.userId() }
            return CorrectionResponse(
                result.id ?: Entity.UNKNOWN_ID,
                result.examType,
                result.examMode,
                sectionResults.map { correction ->
                    CorrectionSectionResponse(
                        correction.id, correction.sectionType, correction.sectionOrder
                    )
                })
        } else {
            throw UnprocessableException("You don't have any exam to correct")
        }
    }
    
    fun getCorrectorCorrectionsByStatus(status: CorrectionStatus): List<CorrectionResponse> {
        val results = resultService.getCorrectorResultsByStatus(
            status, Auth.userId()
        )
        return results.map { result ->
            val sectionResults = sectionResultService.getByResultId(
                result.id ?: Entity.UNKNOWN_ID
            ).filter { it.correctorUserId == Auth.userId() }
            CorrectionResponse(
                result.id ?: Entity.UNKNOWN_ID,
                result.examType,
                result.examMode,
                sectionResults.map { correction ->
                    CorrectionSectionResponse(correction.id, correction.sectionType, correction.sectionOrder)
                })
        }
    }
    
    fun getCorrectorCorrectionExamSessionContent(sectionResultId: Long): CorrectorCorrectionExamSessionContentResponse {
        val sectionResult = sectionResultService.getSectionResultById(sectionResultId)
        val result = resultService.getResultById(sectionResult.resultId)
        if (result.correctorUserId != Auth.userId()) throw UnprocessableException("You don't have access to this correction")
        if (sectionResult.status.order >= CorrectionStatus.PROCESSED.order) throw UnprocessableException("You have already corrected this part")
        
        val answers = answerRepository.findAllByExamSessionIdAndSectionOrder(
            result.examSessionId, sectionResult.sectionOrder
        )
        val examSession = examSessionService.getExamSessionById(result.examSessionId)
        val section = sectionService.getSectionByExamIdAndOrder(examSession.examId, sectionResult.sectionOrder)
        val parts = partService.find(
            Criteria.where("examId").`is`(section.examId).and("sectionId").`is`(section.id)
        )
        return CorrectorCorrectionExamSessionContentResponse(
            examSessionId = result.examSessionId, section = CorrectorCorrectionSectionResponse(header = section.header,
            sectionOrder = section.order,
            sectionType = section.sectionType,
                parts = parts.mapNotNull { part ->
                    val questions = questionService.find(
                        Criteria.where("examId").`is`(section.examId)
                            .and("sectionId").`is`(section.id)
                            .and("partId").`is`(part.id)
                    )
                when (part) {
                    is WritingPartEntity -> {
                        val question = questions.first() as WritingQuestionEntity
                        WritingCorrectorCorrectionPartResponse(
                            partOrder = part.order,
                            question = WritingCorrectorCorrectionQuestionResponse(
                                header = question.header,
                                content = question.content?.let { content ->
                                    examContentService.replaceFileIdsWithDownloadLink(content)
                                }),
                            answer = answers.filterIsInstance<AnswerEntity.TextAnswerEntity>().find { answer ->
                                (answer.partOrder == part.order) && (answer.questionOrder == question.order)
                            }?.let { existingAnswer ->
                                TextCorrectorCorrectionAnswerResponse(
                                    text = existingAnswer.text
                                )
                            }
                        )
                    }
                    
                    is SpeakingPartEntity -> SpeakingCorrectorCorrectionPartResponse(
                        partOrder = part.order,
                        questionAnswerList = questions.map { it as SpeakingQuestionEntity }.map { question ->
                            SpeakingCorrectorCorrectionQuestionAnswerResponse(
                                questionOrder = question.order, question = SpeakingCorrectorCorrectionQuestionResponse(
                                    header = question.header, audioUrl = question.audioId?.let { audioId ->
                                        try {
                                            fileService.generatePublicDownloadLink(
                                                audioId, (24 * 3600)
                                            )
                                        } catch (ex: Exception) {
                                            logger.error("There is an invalid FileId in sectionId = ${section.id} , partOrder = ${part.order} , questionOrder = ${question.order} , audioId = $audioId  ")
                                            null
                                        }
                                    }),
                                answer = answers.filterIsInstance<AnswerEntity.VoiceAnswerEntity>().find { answer ->
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
            })
        )
    }
}