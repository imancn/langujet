package com.cn.langujet.actor.correction.api

import com.cn.langujet.actor.correction.payload.request.AssignCorrectionRequest
import com.cn.langujet.actor.correction.payload.response.CorrectionResponse
import com.cn.langujet.actor.correction.payload.response.CorrectorAvailableCorrectionResponse
import com.cn.langujet.actor.correction.payload.response.CorrectorCorrectionExamSessionContentResponse
import com.cn.langujet.domain.correction.model.CorrectionStatus
import com.cn.langujet.domain.correction.model.CorrectorType
import com.cn.langujet.domain.correction.service.CorrectionService
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/corrector/corrections")
@Validated
class CorrectionCorrectorController(
    private val correctionService: CorrectionService
) {
    @GetMapping("/pending")
    @PreAuthorize("hasRole('CORRECTOR')")
    fun getCorrectorPendingCorrections(): List<CorrectorAvailableCorrectionResponse> {
        return correctionService.getCorrectorPendingCorrections(CorrectorType.HUMAN)
    }
    
    @PostMapping("/assign")
    @PreAuthorize("hasRole('CORRECTOR')")
    fun assignCorrection(
        @RequestBody assignCorrectionRequest: AssignCorrectionRequest
    ): CorrectionResponse {
        return correctionService.assignCorrectionByCorrector(assignCorrectionRequest, CorrectorType.HUMAN)
    }
    
    @GetMapping("/processing")
    @PreAuthorize("hasRole('CORRECTOR')")
    fun getCorrectorProcessingCorrection(): CorrectionResponse {
        return correctionService.getCorrectorProcessingCorrection()
    }
    
    @GetMapping
    @PreAuthorize("hasRole('CORRECTOR')")
    fun getCorrectorCorrectionsByStatus(@RequestParam status: CorrectionStatus): List<CorrectionResponse> {
        return correctionService.getCorrectorCorrectionsByStatus(status)
    }
    
    @GetMapping("/exam-session-contents/{correctionId}")
    @PreAuthorize("hasRole('CORRECTOR')")
    fun getCorrectorCorrectionExamSessionContent(
        @PathVariable correctionId: Long
    ): CorrectorCorrectionExamSessionContentResponse {
        return correctionService.getCorrectorCorrectionExamSessionContent(correctionId)
    }
}