package com.cn.langujet.domain.result.service

import com.cn.langujet.actor.result.payload.request.SubmitCorrectorResultRequest
import com.cn.langujet.actor.result.payload.response.DetailedResultResponse
import com.cn.langujet.application.arch.Auth
import com.cn.langujet.application.arch.advice.InvalidCredentialException
import com.cn.langujet.application.arch.advice.UnprocessableException
import com.cn.langujet.application.arch.models.entity.Entity
import com.cn.langujet.application.arch.mongo.HistoricalEntityService
import com.cn.langujet.application.service.file.domain.service.FileService
import com.cn.langujet.domain.correction.model.CorrectionStatus
import com.cn.langujet.domain.correction.model.CorrectorType
import com.cn.langujet.domain.exam.model.ExamSessionEntity
import com.cn.langujet.domain.exam.model.enums.ExamType
import com.cn.langujet.domain.exam.service.ExamSessionService
import com.cn.langujet.domain.result.model.ResultEntity
import com.cn.langujet.domain.result.model.SectionResultEntity
import com.cn.langujet.domain.result.repository.ResultRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Service
import java.util.*
import kotlin.math.ceil

@Service
class ResultService(
    override var repository: ResultRepository,
    private val sectionResultService: SectionResultService
) : HistoricalEntityService<ResultRepository, ResultEntity>() {
    @Autowired
    private lateinit var fileService: FileService
    
    @Autowired @Lazy
    private lateinit var examSessionService: ExamSessionService
    
    fun initiateResult(examSession: ExamSessionEntity): ResultEntity {
        return save(
            ResultEntity(
                id = null,
                examSessionId = examSession.id ?: Entity.UNKNOWN_ID,
                examType = examSession.examType,
                examMode = examSession.examMode,
                correctorType = examSession.correctorType,
                correctorUserId = null,
                status = CorrectionStatus.PENDING,
                score = null,
                recommendation = null
            )
        )
    }
    
    fun assignResultToCorrector(result: ResultEntity, correctorUserId: Long): ResultEntity {
        return save(
            result.also {
            it.correctorUserId = correctorUserId
            it.status = CorrectionStatus.PROCESSING
            it.updatedAt = Date(System.currentTimeMillis())
        })
    }
    
    fun getStudentDetailedResultByExamSessionId(examSessionId: Long): DetailedResultResponse {
        val examSession = examSessionService.getExamSessionById(examSessionId)
        if (Auth.userId() != examSession.studentUserId) {
            throw InvalidCredentialException("This Exam Session is not belong to you")
        }
        val result = getResultByExamSessionId(examSessionId).orElseThrow { UnprocessableException("Result not found") }
        val sectionResults =
            sectionResultService.getSectionResultsByResultId(result.id ?: Entity.UNKNOWN_ID).map { sr ->
                Pair(
                    sr,
                    sr.attachmentFileId?.let {
                        fileService.generatePublicDownloadLink(it, 86400)
                    }
                )
        }
        return DetailedResultResponse(result, sectionResults)
    }
    
    fun getCorrectorDetailedResultByExamCorrectionId(examCorrectionId: Long): DetailedResultResponse {
        val result = getResultById(examCorrectionId)
        if (Auth.isAdmin() || result.correctorUserId == Auth.userId()) {
            val sectionResult =
                sectionResultService.getSectionResultsByResultId(result.id ?: Entity.UNKNOWN_ID).map { sr ->
                    Pair(
                        sr,
                        sr.attachmentFileId?.let {
                            fileService.generatePublicDownloadLink(it, 86400)
                        }
                    )
            }
            return DetailedResultResponse(result, sectionResult)
        } else {
            throw InvalidCredentialException("This Exam Correction is not belong to you")
        }
    }
    
    
    fun submitCorrectorResult(submitCorrectorResultRequest: SubmitCorrectorResultRequest) {
        val sectionResults = sectionResultService.getSectionResultsByResultId(submitCorrectorResultRequest.examCorrectionId)
        sectionResults.forEach {
            if (it.status.order < CorrectionStatus.PROCESSED.order)
                throw UnprocessableException("You should submit all sections correction before this")
        }
        val result = getResultById(submitCorrectorResultRequest.examCorrectionId)
        if (result.correctorUserId != Auth.userId()) {
            throw UnprocessableException("Correction is not belongs to you")
        }
        if (result.status.order >= CorrectionStatus.PROCESSED.order) {
            throw UnprocessableException("Correction had been processed")
        }
        result.recommendation = submitCorrectorResultRequest.recommendation
        result.score = calculateOverAllScore(sectionResults.mapNotNull { it.score }, result.examType)
        // Todo: After Implementation of Approval flow it must be changed
        finalizeCorrection(sectionResults, result)
    }
    
    fun getResultByExamSessionId(examSessionId: Long): Optional<ResultEntity> {
        return repository.findByExamSessionId(examSessionId)
    }
    
    fun getResultById(resultId: Long): ResultEntity {
        return repository.findById(resultId).orElseThrow {
            UnprocessableException("Result not found")
        }
    }
    
    fun getResultsByStatusAndCorrectorType(
        correctionStatus: CorrectionStatus,
        correctorType: CorrectorType
    ): List<ResultEntity> {
        return repository.findByCorrectorTypeAndStatusOrderByCreatedAtAsc(
            correctorType, correctionStatus
        )
    }
    
    fun getCorrectorResultsByStatus(
        correctionStatus: CorrectionStatus, correctorId: Long
    ): List<ResultEntity> {
        return repository.findByStatusAndCorrectorUserIdOrderByCreatedAtAsc(
            correctionStatus, correctorId
        )
    }
    
    fun areAllSectionResultsApproved(result: ResultEntity): Boolean {
        val sectionResults = sectionResultService.getByResultId(result.id ?: Entity.UNKNOWN_ID)
        sectionResults.forEach { 
            if (it.status != CorrectionStatus.APPROVED) {
                return false
            }
        }
        return true
    }
    
    fun finalizeCorrection(sectionResults: List<SectionResultEntity>, result: ResultEntity) {
        result.status = CorrectionStatus.APPROVED
        result.score = calculateOverAllScore(sectionResults.mapNotNull { it.score }, result.examType)
        result.updatedAt = Date(System.currentTimeMillis())
        save(result)
        examSessionService.finalizeCorrection(result.examSessionId)
    }
    
    fun calculateOverAllScore(scores: List<Double>, examType: ExamType): Double {
        return when (examType) {
            ExamType.IELTS_GENERAL, ExamType.IELTS_ACADEMIC -> {
                val score = scores.sumOf { it } / scores.count()
                ceil(score * 2) /2
            }
        }
    }
}