package com.cn.langujet.actor.correction.api

import com.cn.langujet.actor.correction.payload.response.CorrectorAvailableCorrectionResponse
import com.cn.langujet.domain.correction.service.CorrectionService
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/corrector/corrections")
@Validated
class CorrectionController(
    private val correctionService: CorrectionService
) {
    @GetMapping("/pending")
    @PreAuthorize("hasAnyRole('PROFESSOR', 'ADMIN')")
    fun getCorrectorPendingCorrections(): List<CorrectorAvailableCorrectionResponse> {
        return correctionService.getCorrectorPendingCorrections()
    }
}