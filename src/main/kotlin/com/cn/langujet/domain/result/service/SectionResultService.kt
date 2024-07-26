package com.cn.langujet.domain.result.service

import com.cn.langujet.actor.result.payload.request.SubmitCorrectorSectionResultRequest
import com.cn.langujet.actor.util.Auth
import com.cn.langujet.application.advice.FileException
import com.cn.langujet.application.advice.NotFoundException
import com.cn.langujet.application.advice.UnprocessableException
import com.cn.langujet.application.service.file.domain.data.model.FileBucket
import com.cn.langujet.application.service.file.domain.service.FileService
import com.cn.langujet.domain.correction.model.CorrectionStatus
import com.cn.langujet.domain.correction.model.CorrectorType
import com.cn.langujet.domain.exam.model.ExamSessionEntity
import com.cn.langujet.domain.exam.model.SectionType
import com.cn.langujet.domain.exam.repository.dto.SectionMetaDTO
import com.cn.langujet.domain.result.model.SectionResultEntity
import com.cn.langujet.domain.result.repository.SectionResultRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.util.*

@Service
class SectionResultService(
    private val sectionResultRepository: SectionResultRepository,
    private val fileService: FileService,
) {
    @Autowired @Lazy
    private lateinit var resultService: ResultService
    
    fun initSectionResult(
        resultId: String,
        examSession: ExamSessionEntity,
        section: SectionMetaDTO,
    ): SectionResultEntity {
        val now = Date(System.currentTimeMillis())
        return sectionResultRepository.save(
            SectionResultEntity(
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
                attachmentFileId = null,
                createdDate = now,
                updatedDate = now,
            )
        )
    }
    
    fun getSectionResultsByResultId(resultId: String): List<SectionResultEntity> {
        return sectionResultRepository.findAllByResultId(resultId)
    }
    
    fun getSectionResultById(id: String): SectionResultEntity {
        return sectionResultRepository.findById(id).orElseThrow {
            NotFoundException("Section Result not found")
        }
    }
    
    fun getByStatusAndResultId(correctionStatus: CorrectionStatus, resultId: String): List<SectionResultEntity> {
        return sectionResultRepository.findByStatusAndResultId(correctionStatus, resultId)
    }
    
    fun getByResultId(resultId: String): List<SectionResultEntity> {
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
    ): SectionResultEntity {
        val now = Date(System.currentTimeMillis())
        return sectionResultRepository.save(
            SectionResultEntity(
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
                attachmentFileId = null,
                createdDate = now,
                updatedDate = now,
            )
        )
    }
    
    fun assignSectionResultToCorrector(sectionResults: List<SectionResultEntity>, correctorUserId: String) {
        sectionResultRepository.saveAll(
            sectionResults.onEach {
                it.correctorUserId = correctorUserId
                it.status = CorrectionStatus.PROCESSING
            }
        )
    }
    
    fun submitCorrectorSectionResult(submitCorrectorSectionResultRequest: SubmitCorrectorSectionResultRequest) {
        val sectionResult = getSectionResultForSubmission(submitCorrectorSectionResultRequest.sectionCorrectionId)
        sectionResult.score = submitCorrectorSectionResultRequest.score
        sectionResult.recommendation = submitCorrectorSectionResultRequest.recommendation
        sectionResult.updatedDate = Date(System.currentTimeMillis())
        // Todo: After Implementation of Approval flow it should be changed to PROCESSED
        sectionResult.status = CorrectionStatus.APPROVED
        sectionResultRepository.save(sectionResult)
    }
    
    fun attachCorrectorSectionResultFile(attachment: MultipartFile, sectionResultId: String) {
        val sectionResult = getSectionResultForSubmission(sectionResultId)
        val file = fileService.uploadFile(attachment, FileBucket.RESULT_ATTACHMENTS)
        sectionResultRepository.save(
            sectionResult.also {
                it.attachmentFileId = file.id ?: throw FileException("Upload Failed")
            }
        )
    }
    
    private fun getSectionResultForSubmission(sectionResultId: String): SectionResultEntity {
        val sectionResult = getSectionResultById(sectionResultId)
        if (sectionResult.correctorUserId != Auth.userId()) {
            throw UnprocessableException("Correction is not belongs to you")
        }
        val result = resultService.getResultById(sectionResult.resultId)
        if (result.status.order >= CorrectionStatus.PROCESSED.order) {
            throw UnprocessableException("Correction had been processed")
        }
        return sectionResult
    }
}