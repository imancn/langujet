package com.cn.langujet.domain.correction.service

import com.cn.langujet.actor.correction.payload.response.CorrectorAvailableCorrectionResponse
import com.cn.langujet.application.advice.NotFoundException
import com.cn.langujet.domain.correction.model.CorrectionEntity
import com.cn.langujet.domain.correction.model.CorrectionStatus
import com.cn.langujet.domain.correction.model.CorrectionType
import com.cn.langujet.domain.correction.repository.CorrectAnswerRepository
import com.cn.langujet.domain.correction.repository.CorrectionRepository
import com.cn.langujet.domain.correction.service.corrector.AutoCorrectorService
import com.cn.langujet.domain.exam.model.ExamSession
import com.cn.langujet.domain.exam.service.ExamVariantService
import com.cn.langujet.domain.exam.service.SectionService
import org.springframework.stereotype.Service

@Service
class CorrectionService(
    private val correctionRepository: CorrectionRepository,
    private val examVariantService: ExamVariantService,
    private val correctAnswerRepository: CorrectAnswerRepository,
    private val autoCorrectorService: AutoCorrectorService,
    private val sectionService: SectionService,
) {
    fun makeExamSessionCorrection(examSession: ExamSession) {
        val examVariant = examVariantService.getExamVariantById(examSession.examVariantId)
        val sections = sectionService.getSectionsMetaData(examSession.examId)
        val corrections = sections.map { section ->
            val correctionType = if (correctAnswerRepository.existsByExamIdAndSectionOrder(examSession.examId, section.order)) {
                CorrectionType.AUTO_CORRECTION
            } else {
                examVariant.correctionType
            }
            CorrectionEntity(
                correctionType,
                CorrectionStatus.PENDING,
                examSession.id ?: "",
                examVariant.examType,
                section.order,
                section.sectionType
            )
        }
        correctionRepository.saveAll(corrections).forEach {
            if (it.type == CorrectionType.AUTO_CORRECTION)
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
    
    fun getCorrectionByExamSessionIdAndSectionOrder(examSessionId: String, sectionOrder: Int): CorrectionEntity {
        return correctionRepository.findAllByExamSessionIdAndSectionOrder(examSessionId, sectionOrder).orElseThrow {
            NotFoundException("Correction with examSessionId: $examSessionId and sectionOrder: $sectionOrder not found")
        }
    }
    
    fun areAllSectionCorrectionProcessed(examSessionId: String): Boolean {
        getCorrectionsByExamSessionId(examSessionId).forEach {
            if (it.status != CorrectionStatus.PROCESSED) return false
        }
        return true
    }
    
    fun getCorrectorPendingCorrections(): List<CorrectorAvailableCorrectionResponse> {
        val responses = mutableListOf<CorrectorAvailableCorrectionResponse>()
        correctionRepository.findByTypeAndStatusOrderByCreatedDateAsc(
            CorrectionType.PROFESSOR, CorrectionStatus.PENDING
        ).groupBy {
            it.examSessionId
        }.forEach { (examSessionId, corrections) ->
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
}
