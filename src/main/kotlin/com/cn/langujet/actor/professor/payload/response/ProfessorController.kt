package com.cn.langujet.actor.professor.payload.response

import com.cn.langujet.actor.util.toOkResponseEntity
import com.cn.langujet.domain.professor.ProfessorService
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/professor")
@Validated
class ProfessorController(
    val professorService: ProfessorService,
) {

    @GetMapping("/profile")
    @PreAuthorize("hasRole('PROFESSOR')")
    fun getProfile(
        @RequestHeader("Authorization") auth: String?
    ): ResponseEntity<ProfessorProfileResponse> =
        toOkResponseEntity(ProfessorProfileResponse(professorService.getProfessorByAuthToken(auth)))

    @PutMapping("/profile")
    @PreAuthorize("hasRole('PROFESSOR')")
    fun editProfile(
        @RequestHeader("Authorization") auth: String?,
        @RequestParam fullName: String?,
        @RequestParam biography: String?,
        @RequestParam ieltsScore: Double?,
        @RequestParam credit: Double?
    ): ResponseEntity<ProfessorProfileResponse> =
        toOkResponseEntity(professorService.editProfile(auth, fullName, biography, ieltsScore, credit))
}