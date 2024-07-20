package com.cn.langujet.domain.result.service

import com.cn.langujet.actor.result.payload.request.SubmitCorrectorResultRequest
import com.cn.langujet.actor.result.payload.response.DetailedResultResponse
import com.cn.langujet.actor.util.Auth
import com.cn.langujet.application.advice.InvalidTokenException
import com.cn.langujet.application.advice.NotFoundException
import com.cn.langujet.application.advice.UnprocessableException
import com.cn.langujet.domain.correction.model.CorrectionStatus
import com.cn.langujet.domain.correction.model.CorrectorType
import com.cn.langujet.domain.correction.service.corrector.ScoreCalculator.Companion.calculateOverAllScore
import com.cn.langujet.domain.exam.model.ExamSession
import com.cn.langujet.domain.exam.service.ExamSessionService
import com.cn.langujet.domain.result.model.Result
import com.cn.langujet.domain.result.repository.ResultRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Service
import java.util.*

@Service
class ResultService(
    private val resultRepository: ResultRepository,
    private val sectionResultService: SectionResultService
) {
    @Autowired @Lazy
    private lateinit var examSessionService: ExamSessionService
    
    fun initiateResult(examSession: ExamSession): Result {
        val now = Date(System.currentTimeMillis())
        return resultRepository.save(
            Result(
                id = null,
                examSessionId = examSession.id ?: "",
                examType = examSession.examType,
                examMode = examSession.examMode,
                correctorType = examSession.correctorType,
                correctorUserId = null,
                status = CorrectionStatus.PENDING,
                score = null,
                recommendation = null,
                createdDate = now,
                updatedDate = now,
            )
        )
    }
    
    fun assignResultToCorrector(result: Result, correctorUserId: String): Result {
        return resultRepository.save(
            result.also {
            it.correctorUserId = correctorUserId
            it.status = CorrectionStatus.PROCESSING
        })
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
    
    fun getResultById(resultId: String): Result {
        return resultRepository.findById(resultId).orElseThrow {
            NotFoundException("Result not found")
        }
    }
    
    fun submitCorrectorResult(submitCorrectorResultRequest: SubmitCorrectorResultRequest) {
        val sectionResults = sectionResultService.getSectionResultsByResultId(submitCorrectorResultRequest.resultId)
        sectionResults.forEach {
            if (it.status.order < CorrectionStatus.PROCESSED.order)
                throw UnprocessableException("You should submit all sections correction before this")
        }
        val result = getResultById(submitCorrectorResultRequest.resultId)
        if (result.correctorUserId != Auth.userId()) {
            throw UnprocessableException("Correction is not belongs to you")
        }
        if (result.status.order >= CorrectionStatus.PROCESSED.order) {
            throw UnprocessableException("Correction had been processed")
        }
        result.recommendation = submitCorrectorResultRequest.recommendation
        result.score = calculateOverAllScore(sectionResults.mapNotNull { it.score }, result.examType)
        // Todo: After Implementation of Approval flow it must be changed
        finalizeCorrection(result)
    }
    
    fun getHumanResultsByStatus(
        correctionStatus: CorrectionStatus
    ): List<Result> {
        return resultRepository.findByCorrectorTypeAndStatusOrderByCreatedDateAsc(
            CorrectorType.HUMAN, correctionStatus
        )
    }
    
    fun getCorrectorResultsByStatus(
        correctionStatus: CorrectionStatus, correctorId: String
    ): List<Result> {
        return resultRepository.findByStatusAndCorrectorUserIdOrderByCreatedDateAsc(
            correctionStatus, correctorId
        )
    }
    
    fun areAllSectionResultsApproved(result: Result): Boolean {
        val sectionResults = sectionResultService.getByResultId(result.id ?: "")
        sectionResults.forEach { 
            if (it.status != CorrectionStatus.APPROVED) {
                return false
            }
        }
        return true
    }
    
    fun finalizeCorrection(result: Result) {
        resultRepository.save(
            result.also {
                it.status = CorrectionStatus.APPROVED
                it.updatedDate = Date(System.currentTimeMillis())
            }
        )
        examSessionService.finalizeCorrection(result.examSessionId)
    }
}