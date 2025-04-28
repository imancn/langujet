package com.cn.langujet.actor.correction.api

import com.cn.langujet.actor.correction.payload.request.AssignCorrectionToCorrectorRequest
import com.cn.langujet.actor.correction.payload.request.AssignSpecificCorrectionToCorrectorRequest
import com.cn.langujet.actor.correction.payload.response.CorrectionResponse
import com.cn.langujet.actor.correction.payload.response.CorrectorAvailableCorrectionResponse
import com.cn.langujet.domain.correction.model.CorrectorType
import com.cn.langujet.domain.correction.service.CorrectionService
import com.cn.langujet.domain.user.services.JwtService
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/admin/corrections")
@Validated
class CorrectionAdminController(
    private val jwtService: JwtService,
    private val correctionService: CorrectionService
) {
    @PostMapping("/ai-token")
    @PreAuthorize("hasRole('ADMIN')")
    fun generateAiTokens(
        @RequestParam correctorType: CorrectorType,
        @RequestParam code: Int
    ): Map<String, String> {
        return mapOf(
            "name" to "$correctorType:$code",
            "token" to jwtService.generateAiToken(correctorType, code)
        )
    }
    
    @PostMapping("/assign/exam-session")
    @PreAuthorize("hasRole('ADMIN')")
    fun assignSpecificCorrectionToCorrector(
        @RequestBody assignSpecificCorrectionToCorrectorRequest: AssignSpecificCorrectionToCorrectorRequest
    ): CorrectionResponse {
        return correctionService.assignSpecificCorrectionToCorrector(assignSpecificCorrectionToCorrectorRequest)
    }
    
    @GetMapping("/pending/admin")
    @PreAuthorize("hasAnyRole('CORRECTOR', 'ADMIN')")
    fun getCorrectorPendingCorrections(): List<CorrectorAvailableCorrectionResponse> {
        return correctionService.getCorrectorPendingCorrections(CorrectorType.HUMAN)
    }
    
    @PostMapping("/assign")
    @PreAuthorize("hasRole('ADMIN')")
    fun assignCorrectionToCorrector(
        @RequestBody assignCorrectionToCorrectorRequest: AssignCorrectionToCorrectorRequest
    ): CorrectionResponse {
        return correctionService.assignCorrectionToCorrector(assignCorrectionToCorrectorRequest)
    }
}