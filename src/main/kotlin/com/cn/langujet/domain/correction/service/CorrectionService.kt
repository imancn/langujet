package com.cn.langujet.domain.correction.service

import com.cn.langujet.actor.correction.payload.request.AssignCorrectionRequest
import com.cn.langujet.actor.correction.payload.request.AssignCorrectionToCorrectorRequest
import com.cn.langujet.actor.correction.payload.response.*
import com.cn.langujet.actor.util.Auth
import com.cn.langujet.application.advice.UnprocessableException
import com.cn.langujet.application.service.file.domain.service.FileService
import com.cn.langujet.domain.answer.AnswerRepository
import com.cn.langujet.domain.answer.model.Answer
import com.cn.langujet.domain.correction.model.CorrectionStatus
import com.cn.langujet.domain.correction.repository.CorrectAnswerRepository
import com.cn.langujet.domain.correction.service.corrector.AutoCorrectorService
import com.cn.langujet.domain.correction.service.corrector.ScoreCalculator.Companion.calculateOverAllScore
import com.cn.langujet.domain.corrector.CorrectorService
import com.cn.langujet.domain.exam.model.ExamSession
import com.cn.langujet.domain.exam.model.SpeakingPart
import com.cn.langujet.domain.exam.model.WritingPart
import com.cn.langujet.domain.exam.service.ExamSectionContentService
import com.cn.langujet.domain.exam.service.ExamSessionService
import com.cn.langujet.domain.exam.service.SectionService
import com.cn.langujet.domain.result.service.ResultService
import com.cn.langujet.domain.result.service.SectionResultService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Service
import java.util.*

@Service
class CorrectionService(
    private val correctAnswerRepository: CorrectAnswerRepository,
    private val autoCorrectorService: AutoCorrectorService,
    private val sectionService: SectionService,
    private val correctorService: CorrectorService,
    private val answerRepository: AnswerRepository,
    private val examSectionContentService: ExamSectionContentService,
    private val fileService: FileService,
    private val resultService: ResultService,
    private val sectionResultService: SectionResultService,
) {
    @Autowired @Lazy
    private lateinit var examSessionService: ExamSessionService
    
    private val logger = LoggerFactory.getLogger(this::class.java)
    
    fun initiateExamSessionCorrection(examSession: ExamSession) {
        val sections = sectionService.getSectionsMetaData(examSession.examId)
        val result = resultService.initiateResult(examSession)
        val sectionResults = sections.map { section ->
            if (correctAnswerRepository.existsByExamIdAndSectionOrder(examSession.examId, section.order)) {
                autoCorrectorService.correctExamSection(
                    examSession,
                    result.id ?: "",
                    section.order,
                    section.sectionType
                )
            } else {
                sectionResultService.initSectionResult(result.id ?: "", examSession, section)
            }
        }
        if (resultService.areAllSectionResultsApproved(result)) {
            result.score = calculateOverAllScore(sectionResults.mapNotNull { it.score }, result.examType)
            resultService.finalizeCorrection(result)
        }
    }
    
    fun getCorrectorPendingCorrections(): List<CorrectorAvailableCorrectionResponse> {
        val responses = mutableListOf<CorrectorAvailableCorrectionResponse>()
        resultService.getHumanResultsByStatus(CorrectionStatus.PENDING).forEach { result ->
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
    
    fun assignCorrection(
        assignCorrectionRequest: AssignCorrectionRequest, correctorUserId: String = Auth.userId()
    ): CorrectionResponse {
        if (resultService.getCorrectorResultsByStatus(CorrectionStatus.PROCESSING, correctorUserId).isNotEmpty()) {
            throw UnprocessableException("Finish in-progress exam correction first")
        }
        return try {
            val pendingResult = resultService.getHumanResultsByStatus(CorrectionStatus.PENDING).first {
                it.examMode == assignCorrectionRequest.examMode && it.examType == assignCorrectionRequest.examType
            }
            val sectionCorrections = sectionResultService.getByStatusAndResultId(
                CorrectionStatus.PENDING, pendingResult.id ?: ""
            )
            resultService.assignResultToCorrector(pendingResult, correctorUserId)
            sectionResultService.assignSectionResultToCorrector(sectionCorrections, correctorUserId)
            CorrectionResponse(pendingResult.id ?: "",
                pendingResult.examType,
                pendingResult.examMode,
                sectionCorrections.map { correction ->
                    CorrectionSectionResponse(
                        correction.id, correction.sectionType, correction.sectionOrder
                    )
                })
        } catch (ex: NoSuchElementException) {
            throw UnprocessableException("There is no exam of this type left for correction")
        }
    }
    
    fun getCorrectorProcessingCorrection(): CorrectionResponse {
        val processingResult = resultService.getCorrectorResultsByStatus(
            CorrectionStatus.PROCESSING, Auth.userId()
        ).firstOrNull()
        if (processingResult != null) {
            val processingSectionResults = sectionResultService.getByStatusAndResultId(
                CorrectionStatus.PROCESSING, processingResult.id ?: ""
            )
            return CorrectionResponse(processingResult.id ?: "",
                processingResult.examType,
                processingResult.examMode,
                processingSectionResults.map { correction ->
                    CorrectionSectionResponse(
                        correction.id, correction.sectionType, correction.sectionOrder
                    )
                })
        } else {
            throw UnprocessableException("You don't have any exam to correct")
        }
    }
    
    fun assignCorrectionToCorrector(
        assignCorrectionToCorrectorRequest: AssignCorrectionToCorrectorRequest
    ): CorrectionResponse {
        if (!correctorService.correctorExistsByUserId(assignCorrectionToCorrectorRequest.correctorUserId)) {
            throw UnprocessableException("The user isn't a Corrector")
        }
        return assignCorrection(
            AssignCorrectionRequest(
                assignCorrectionToCorrectorRequest.examType, assignCorrectionToCorrectorRequest.examMode
            ), assignCorrectionToCorrectorRequest.correctorUserId
        )
    }
    
    fun getCorrectorCorrectionExamSessionContent(sectionResultId: String): CorrectorCorrectionExamSessionContentResponse {
        val sectionResult = sectionResultService.getSectionResultById(sectionResultId)
        val result = resultService.getResultById(sectionResult.resultId)
        if (result.correctorUserId != Auth.userId()) throw UnprocessableException("You don't have access to this correction")
        if (sectionResult.status.order >= CorrectionStatus.PROCESSED.order) throw UnprocessableException("You have already corrected this part")
        
        val answers = answerRepository.findAllByExamSessionIdAndSectionOrder(
            result.examSessionId, sectionResult.sectionOrder
        )
        val examSession = examSessionService.getExamSessionById(result.examSessionId)
        val section = sectionService.getSectionByExamIdAndOrder(examSession.examId, sectionResult.sectionOrder)
        return CorrectorCorrectionExamSessionContentResponse(
            examSessionId = result.examSessionId, section = CorrectorCorrectionSectionResponse(header = section.header,
            sectionOrder = section.order,
            sectionType = section.sectionType,
            parts = section.parts.mapNotNull { part ->
                when (part) {
                    is WritingPart -> WritingCorrectorCorrectionPartResponse(
                        partOrder = part.order,
                        question = WritingCorrectorCorrectionQuestionResponse(header = part.question.header,
                            content = part.question.content?.let { content ->
                                examSectionContentService.replaceFileIdsWithDownloadLink(content)
                            }),
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
                                question = SpeakingCorrectorCorrectionQuestionResponse(header = question.header,
                                audioId = question.audioId?.let { audioId ->
                                    try {
                                        fileService.generatePublicDownloadLink(
                                            audioId, (24 * 3600)
                                        )
                                    } catch (ex: Exception) {
                                        logger.error("There is an invalid FileId in sectionId = ${section.id} , partOrder = ${part.order} , questionOrder = ${question.order} , audioId = $audioId  ")
                                        null
                                    }
                                }),
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
            })
        )
    }
}