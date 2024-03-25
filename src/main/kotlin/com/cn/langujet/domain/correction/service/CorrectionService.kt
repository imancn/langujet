package com.cn.langujet.domain.correction.service

import com.cn.langujet.application.advice.NotFoundException
import com.cn.langujet.domain.correction.model.CorrectionEntity
import com.cn.langujet.domain.correction.model.CorrectionStatus
import com.cn.langujet.domain.correction.model.CorrectionType
import com.cn.langujet.domain.correction.repository.CorrectAnswerRepository
import com.cn.langujet.domain.correction.repository.CorrectionRepository
import com.cn.langujet.domain.correction.service.corrector.AutoCorrectorService
import com.cn.langujet.domain.exam.model.ExamSession
import com.cn.langujet.domain.exam.service.ExamVariantService
import org.springframework.stereotype.Service

@Service
class CorrectionService(
    private val correctionRepository: CorrectionRepository,
    private val examVariantService: ExamVariantService,
    private val correctAnswerRepository: CorrectAnswerRepository,
    private val autoCorrectorService: AutoCorrectorService
) {
    fun makeExamSessionCorrection(examSession: ExamSession) {
        val examVariant = examVariantService.getExamVariantById(examSession.examVariantId)
        val corrections = examSession.sectionOrders.map { sectionOrder ->
            if (correctAnswerRepository.existsByExamIdAndSectionOrder(examSession.examId, sectionOrder)) {
                CorrectionEntity(CorrectionType.AUTO_CORRECTION, CorrectionStatus.PENDING, examSession.id ?: "", sectionOrder)
            } else {
                CorrectionEntity(examVariant.correctionType, CorrectionStatus.PENDING, examSession.id ?: "", sectionOrder)
            }
        }
        correctionRepository.saveAll(corrections).forEach {
            if (it.type == CorrectionType.AUTO_CORRECTION)
                autoCorrectorService.correctExamSection(examSession, it.sectionOrder)
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
}
