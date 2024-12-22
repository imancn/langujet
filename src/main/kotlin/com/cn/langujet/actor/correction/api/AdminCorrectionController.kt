package com.cn.langujet.actor.correction.api

import com.cn.langujet.domain.correction.model.CorrectorType
import com.cn.langujet.domain.user.services.JwtService
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/admin")
@Validated
class AdminCorrectionController(private val jwtService: JwtService) {
    @PostMapping("/corrections/ai-token")
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
}