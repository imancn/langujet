package com.cn.langujet.domain.result.service

import com.cn.langujet.actor.result.payload.request.SubmitCorrectorSectionResultRequest
import com.cn.langujet.actor.util.Auth
import com.cn.langujet.application.advice.NotFoundException
import com.cn.langujet.application.advice.UnprocessableException
import com.cn.langujet.domain.correction.model.CorrectionStatus
import com.cn.langujet.domain.correction.model.CorrectorType
import com.cn.langujet.domain.exam.model.ExamSession
import com.cn.langujet.domain.exam.model.SectionType
import com.cn.langujet.domain.exam.repository.dto.SectionMetaDTO
import com.cn.langujet.domain.result.model.SectionResult
import com.cn.langujet.domain.result.repository.SectionResultRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class SectionResultService(
    private val sectionResultRepository: SectionResultRepository,
) {
    fun initSectionResult(
        resultId: String,
        examSession: ExamSession,
        section: SectionMetaDTO,
    ): SectionResult {
        val now = Date(System.currentTimeMillis())
        return sectionResultRepository.save(
            SectionResult(
                id = null,
                resultId = resultId,
                examSessionId = examSession.id ?: "",
                sectionOrder = section.order,
                sectionType = section.sectionType,
                correctorType = examSession.correctorType,
                correctorUserId = null,
                status = CorrectionStatus.PENDING,
                correctIssuesCount = null,
                score = null,
                recommendation = null,
                createdDate = now,
                updatedDate = now,
            )
        )
    }
    
    fun getSectionResultsByResultId(resultId: String): List<SectionResult> {
        return sectionResultRepository.findAllByResultId(resultId)
    }
    
    fun getSectionResultById(id: String): SectionResult {
        return sectionResultRepository.findById(id).orElseThrow {
            NotFoundException("Section Result not found")
        }
    }
    
    fun getByStatusAndResultId(correctionStatus: CorrectionStatus, resultId: String): List<SectionResult> {
        return sectionResultRepository.findByStatusAndResultId(correctionStatus, resultId)
    }
    
    fun getByResultId(resultId: String): List<SectionResult> {
        return sectionResultRepository.findByResultId(resultId)
    }
    
    fun addAutoCorrectionSectionResult(
        resultId: String,
        examSessionId: String,
        sectionOrder: Int,
        sectionType: SectionType,
        correctIssuesCount: Int?,
        score: Double,
        recommendation: String? = null
    ): SectionResult {
        val now = Date(System.currentTimeMillis())
        return sectionResultRepository.save(
            SectionResult(
                id = null,
                resultId = resultId,
                examSessionId = examSessionId,
                sectionOrder = sectionOrder,
                sectionType = sectionType,
                correctorType = CorrectorType.AUTO_CORRECTION,
                correctorUserId = null,
                status = CorrectionStatus.APPROVED,
                correctIssuesCount = correctIssuesCount,
                score = score,
                recommendation = recommendation,
                createdDate = now,
                updatedDate = now,
            )
        )
    }
    
    fun assignSectionResultToCorrector(sectionResults: List<SectionResult>, correctorUserId: String) {
        sectionResultRepository.saveAll(
            sectionResults.onEach {
                it.correctorUserId = correctorUserId
                it.status = CorrectionStatus.PROCESSING
            }
        )
    }
    
    fun submitCorrectorSectionResult(submitCorrectorSectionResultRequest: SubmitCorrectorSectionResultRequest) {
        val sectionResult = getSectionResultById(submitCorrectorSectionResultRequest.sectionResultId)
        if (sectionResult.correctorUserId != Auth.userId()) {
            throw UnprocessableException("Correction is not belongs to you")
        }
        if (sectionResult.status.order >= CorrectionStatus.PROCESSED.order) {
            throw UnprocessableException("Correction had been processed")
        }
        sectionResult.score = submitCorrectorSectionResultRequest.score
        sectionResult.recommendation = submitCorrectorSectionResultRequest.recommendation
        sectionResult.updatedDate = Date(System.currentTimeMillis())
        // Todo: After Implementation of Approval flow it should be changed to PROCESSED
        sectionResult.status = CorrectionStatus.APPROVED
        sectionResultRepository.save(sectionResult)
    }
}