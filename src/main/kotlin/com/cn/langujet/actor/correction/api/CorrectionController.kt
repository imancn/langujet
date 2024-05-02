package com.cn.langujet.actor.correction.api

import com.cn.langujet.actor.correction.payload.response.CorrectionResponse
import com.cn.langujet.actor.correction.payload.request.AssignCorrectionRequest
import com.cn.langujet.actor.correction.payload.request.AssignCorrectionToCorrectorRequest
import com.cn.langujet.actor.correction.payload.response.CorrectorAvailableCorrectionResponse
import com.cn.langujet.domain.correction.service.CorrectionService
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

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
}