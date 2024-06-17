package com.cn.langujet.actor.corrector.api

import com.cn.langujet.actor.corrector.payload.response.CorrectorProfileResponse
import com.cn.langujet.actor.util.Auth
import com.cn.langujet.actor.util.toOkResponseEntity
import com.cn.langujet.domain.corrector.CorrectorService
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/corrector")
@Validated
class CorrectorController(
    val correctorService: CorrectorService,
) {

    @GetMapping("/profile")
    @PreAuthorize("hasRole('CORRECTOR')")
    fun getProfile(): ResponseEntity<CorrectorProfileResponse> =
        toOkResponseEntity(CorrectorProfileResponse(correctorService.getCorrectorByUserId(Auth.userId())))

    @PostMapping("/profile")
    @PreAuthorize("hasRole('CORRECTOR')")
    fun editProfile(
        @RequestParam fullName: String?,
        @RequestParam biography: String?,
        @RequestParam ieltsScore: Double?,
        @RequestParam credit: Double?
    ): ResponseEntity<CorrectorProfileResponse> =
        toOkResponseEntity(correctorService.editProfile(fullName, biography, ieltsScore, credit))
}