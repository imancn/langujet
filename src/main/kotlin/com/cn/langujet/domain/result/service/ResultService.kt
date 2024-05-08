package com.cn.langujet.domain.result.service

import com.cn.langujet.actor.result.payload.request.AddCorrectorSectionResultRequest
import com.cn.langujet.actor.result.payload.response.DetailedResultResponse
import com.cn.langujet.actor.util.Auth
import com.cn.langujet.application.advice.InvalidTokenException
import com.cn.langujet.application.advice.LogicalException
import com.cn.langujet.application.advice.NotFoundException
import com.cn.langujet.domain.correction.model.CorrectionEntity
import com.cn.langujet.domain.correction.model.CorrectionStatus
import com.cn.langujet.domain.correction.service.CorrectionService
import com.cn.langujet.domain.exam.model.ExamType
import com.cn.langujet.domain.exam.service.ExamSessionService
import com.cn.langujet.domain.result.model.Result
import com.cn.langujet.domain.result.model.SectionResult
import com.cn.langujet.domain.result.repository.ResultRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Service

@Service
class ResultService(
    private val resultRepository: ResultRepository,
    private val correctionService: CorrectionService,
    private val sectionResultService: SectionResultService
) {
    @Autowired
    @Lazy
    private lateinit var examSessionService: ExamSessionService
    
    fun initiateResult(examSessionId: String, examType: ExamType) {
        resultRepository.save(
            Result(
                id = null,
                examSessionId = examSessionId,
                examType = examType,
                score = null,
                recommendation = null,
            )
        )
    }
    
    fun getDetailedResultByExamSessionId(examSessionId: String): DetailedResultResponse {
        val examSession = examSessionService.getExamSessionById(examSessionId)
        if (Auth.userId() != examSession.studentUserId) {
            throw InvalidTokenException("Exam Session with id: $examSessionId is not belong to you")
        }
        val result = getResultByExamSessionId(examSessionId)
        val sectionResult = sectionResultService.getSectionResultsByResultId(result.id ?: "")
        return DetailedResultResponse(result, sectionResult)
    }
    
    fun getResultByExamSessionId(examSessionId: String): Result {
        return resultRepository.findByExamSessionId(examSessionId).orElseThrow {
            NotFoundException("Result not found")
        }
    }
    
    fun addCorrectorSectionResult(addCorrectorSectionResultRequest: AddCorrectorSectionResultRequest) {
        val correction = correctionService.getCorrectorCorrectionEntity(addCorrectorSectionResultRequest.correctionId)
        addSectionResult(
            correction = correction,
            correctIssuesCount = null,
            score = addCorrectorSectionResultRequest.score,
            recommendation = addCorrectorSectionResultRequest.recommendation,
        )
    }
    
    fun addSectionResult(
        correction: CorrectionEntity,
        correctIssuesCount: Int?,
        score: Double,
        recommendation: String? = null
    ) {
        val result = getResultByExamSessionId(correction.examSessionId)
        
        if (correction.status == CorrectionStatus.PROCESSED) {
            throw LogicalException("Section with ExamSessionId: ${result.examSessionId} and SectionOrder: ${correction.sectionOrder} has been processed")
        }
        
        sectionResultService.createSectionResult(
            SectionResult(
                id = null,
                resultId = result.id ?: "",
                sectionOrder = correction.sectionOrder,
                sectionType = correction.sectionType,
                correctIssuesCount = correctIssuesCount,
                score = score,
                recommendation = recommendation
            )
        )
        correctionService.changeStatus(correction, CorrectionStatus.PROCESSED)
        if (correctionService.areAllSectionCorrectionProcessed(result.examSessionId)) {
            examSessionService.finalizeCorrection(result.examSessionId)
            val sectionResults = sectionResultService.getSectionResultsByResultId(result.id ?: "")
            val overAllScore = calculateOverAllScore(sectionResults.map { it.score }, result.examType)
            resultRepository.save(result.also { it.score = overAllScore })
        }
    }
    
    private fun calculateOverAllScore(scores: List<Double>, examType: ExamType): Double {
        return when (examType) {
            ExamType.IELTS_GENERAL, ExamType.IELTS_ACADEMIC -> {
                scores.sumOf { it } / scores.count()
            }
        }
    }
}