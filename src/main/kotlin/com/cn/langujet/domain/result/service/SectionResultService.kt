package com.cn.langujet.domain.result.service

import com.cn.langujet.actor.result.payload.request.SubmitCorrectorSectionResultRequest
import com.cn.langujet.application.arch.Auth
import com.cn.langujet.application.arch.advice.InternalServerError
import com.cn.langujet.application.arch.advice.UnprocessableException
import com.cn.langujet.application.arch.models.entity.Entity
import com.cn.langujet.application.arch.mongo.HistoricalEntityService
import com.cn.langujet.application.service.file.domain.data.model.FileBucket
import com.cn.langujet.application.service.file.domain.service.FileService
import com.cn.langujet.domain.correction.model.CorrectionStatus
import com.cn.langujet.domain.correction.model.CorrectorType
import com.cn.langujet.domain.exam.model.ExamSessionEntity
import com.cn.langujet.domain.exam.model.enums.SectionType
import com.cn.langujet.domain.exam.repository.dto.SectionMetaDTO
import com.cn.langujet.domain.result.model.SectionResultEntity
import com.cn.langujet.domain.result.repository.SectionResultRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class SectionResultService(
    override var repository: SectionResultRepository,
    private val fileService: FileService,
) : HistoricalEntityService<SectionResultRepository, SectionResultEntity>() {
    @Autowired @Lazy
    private lateinit var resultService: ResultService
    
    fun initSectionResult(
        resultId: Long,
        examSession: ExamSessionEntity,
        section: SectionMetaDTO,
    ): SectionResultEntity {
        return save(
            SectionResultEntity(
                id = null,
                resultId = resultId,
                examSessionId = examSession.id ?: Entity.UNKNOWN_ID,
                sectionOrder = section.order,
                sectionType = section.sectionType,
                correctorType = examSession.correctorType,
                correctorUserId = null,
                status = CorrectionStatus.PENDING,
                correctIssuesCount = null,
                score = null,
                recommendation = null,
                attachmentFileId = null
            )
        )
    }
    
    fun getSectionResultsByResultId(resultId: Long): List<SectionResultEntity> {
        return repository.findByResultId(resultId)
    }
    
    fun getSectionResultById(id: Long): SectionResultEntity {
        return repository.findById(id).orElseThrow {
            UnprocessableException("Section Result not found")
        }
    }
    
    fun getByStatusAndResultId(correctionStatus: CorrectionStatus, resultId: Long): List<SectionResultEntity> {
        return repository.findByStatusAndResultId(correctionStatus, resultId)
    }
    
    fun getByResultId(resultId: Long): List<SectionResultEntity> {
        return repository.findByResultId(resultId)
    }
    
    fun addAutoCorrectionSectionResult(
        resultId: Long,
        examSessionId: Long,
        sectionOrder: Int,
        sectionType: SectionType,
        correctIssuesCount: Int?,
        score: Double,
        recommendation: String? = null
    ): SectionResultEntity {
        return save(
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
                attachmentFileId = null
            )
        )
    }
    
    fun assignSectionResultToCorrector(sectionResults: List<SectionResultEntity>, correctorUserId: Long) {
        saveMany(
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
        // Todo: After Implementation of Approval flow it should be changed to PROCESSED
        sectionResult.status = CorrectionStatus.APPROVED
        save(sectionResult)
    }
    
    fun attachCorrectorSectionResultFile(attachment: MultipartFile, sectionResultId: Long) {
        val sectionResult = getSectionResultForSubmission(sectionResultId)
        val extension = attachment.originalFilename?.substringAfterLast('.', "")
        val contentType = "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
        val extensionWhiteList = listOf("docx", "doc")
        if (attachment.contentType == contentType && extensionWhiteList.contains(extension)) {
            val file = fileService.uploadFile(attachment, FileBucket.RESULT_ATTACHMENTS)
            save(
                sectionResult.also {
                    it.attachmentFileId = file.id ?: throw InternalServerError("Upload Failed")
                }
            )
        } else {
            throw UnprocessableException("Invalid File Format")
        }
    }
    
    private fun getSectionResultForSubmission(sectionResultId: Long): SectionResultEntity {
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