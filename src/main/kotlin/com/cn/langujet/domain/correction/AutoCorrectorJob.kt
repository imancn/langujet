package com.cn.langujet.domain.correction

import com.cn.langujet.domain.correction.model.CorrectionEntity
import com.cn.langujet.domain.correction.model.CorrectionStatus
import com.cn.langujet.domain.correction.service.CorrectionService
import com.cn.langujet.domain.correction.service.corrector.AutoCorrectorService
import org.slf4j.LoggerFactory
import org.springframework.data.domain.PageRequest
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class AutoCorrectorJob(
    private val correctionService: CorrectionService,
    private val autoCorrectorService: AutoCorrectorService,
) {
    private val logger = LoggerFactory.getLogger(javaClass.simpleName)
    var processing: Boolean = false
    
    @Scheduled(initialDelay = 5_000, fixedRate = (10 * 60 * 1000))
    private fun autoCorrectionSchedule() {
        if (processing) {
            logger.warn("CorrectorSchedule process took over 10 minutes")
            return
        } else {
            processing = true
        }
        
        var pageRequest = PageRequest.of(0, 20)
        do {
            val corrections = correctionService.getAutoCorrectionPendingCorrections(pageRequest)
            corrections.forEach(this::processCorrection)
            pageRequest = pageRequest.next()
        } while (corrections.hasNext())
        
        processing = false
    }
    
    private fun processCorrection(correctionEntity: CorrectionEntity) {
        try {
            correctionService.changeStatus(correctionEntity, CorrectionStatus.PROCESSING)
            autoCorrectorService.correctExamSection(correctionEntity.examSessionId, correctionEntity.sectionOrder)
            logger.info("Correction processed for Correction Entity with id: ${correctionEntity.id}")
        } catch (ex: Exception) {
            logger.error("Correction process failed for Correction Entity with id: ${correctionEntity.id}")
            ex.printStackTrace()
            correctionService.changeStatus(correctionEntity, CorrectionStatus.PENDING)
        }
    }
}