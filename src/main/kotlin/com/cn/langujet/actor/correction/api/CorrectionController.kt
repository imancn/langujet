package com.cn.langujet.actor.correction.api

import com.cn.langujet.actor.correction.payload.response.CorrectionResponse
import com.cn.langujet.actor.correction.payload.request.AssignCorrectionRequest
import com.cn.langujet.actor.correction.payload.request.AssignCorrectionToCorrectorRequest
import com.cn.langujet.actor.correction.payload.response.CorrectorAvailableCorrectionResponse
import com.cn.langujet.actor.correction.payload.response.CorrectorCorrectionExamSessionContentResponse
import com.cn.langujet.domain.correction.service.CorrectionService
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/")
@Validated
class CorrectionController(
    private val correctionService: CorrectionService
) {
    @GetMapping("/corrector/corrections/pending")
    @PreAuthorize("hasAnyRole('CORRECTOR', 'ADMIN')")
    fun getCorrectorPendingCorrections(): List<CorrectorAvailableCorrectionResponse> {
        return correctionService.getCorrectorPendingCorrections()
    }
    
    @PostMapping("/corrector/corrections/assign")
    @PreAuthorize("hasRole('CORRECTOR')")
    fun assignCorrection(
        @RequestBody assignCorrectionRequest: AssignCorrectionRequest
    ): CorrectionResponse {
        return correctionService.assignCorrection(assignCorrectionRequest)
    }
    
    @PostMapping("/admin/corrections/assign")
    @PreAuthorize("hasRole('ADMIN')")
    fun assignCorrectionToCorrector(
        @RequestBody assignCorrectionToCorrectorRequest: AssignCorrectionToCorrectorRequest
    ): CorrectionResponse {
        return correctionService.assignCorrectionToCorrector(assignCorrectionToCorrectorRequest)
    }
    
    @GetMapping("/corrector/corrections/processing")
    @PreAuthorize("hasRole('CORRECTOR')")
    fun getCorrectorProcessingCorrection(): CorrectionResponse {
        return correctionService.getCorrectorProcessingCorrection()
    }
    
    @GetMapping("/corrector/corrections/exam-session-content/{correctionId}")
    @PreAuthorize("hasRole('CORRECTOR')")
    fun getCorrectorCorrectionExamSessionContent(
        @PathVariable correctionId: String
    ): CorrectorCorrectionExamSessionContentResponse {
        return correctionService.getCorrectorCorrectionExamSessionContent(correctionId)
    }
}