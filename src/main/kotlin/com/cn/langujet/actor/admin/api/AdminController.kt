package com.cn.langujet.actor.admin.api

import com.cn.langujet.actor.admin.payload.response.ConfirmExamResponse
import com.cn.langujet.actor.util.toOkResponseEntity
import com.cn.langujet.domain.admin.AdminService
import com.cn.langujet.domain.exam.model.ExamRequest
import com.cn.langujet.domain.professor.Professor
import jakarta.validation.constraints.NotBlank
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/admin")
@Validated
class AdminController(
    val adminService: AdminService,
) {
    @GetMapping("/get-professors")
    @PreAuthorize("hasRole('ADMIN')")
    fun getProfessors(@RequestHeader("Authorization") auth: String?): ResponseEntity<List<Professor>> =
        toOkResponseEntity(adminService.getProfessors())

    @GetMapping("/get-exam-requests")
    @PreAuthorize("hasRole('ADMIN')")
    fun getExamRequests(@RequestHeader("Authorization") auth: String?): ResponseEntity<List<ExamRequest>> =
        toOkResponseEntity(adminService.getExamRequests())

    @PostMapping("/confirm-exam-request")
    @PreAuthorize("hasRole('ADMIN')")
    fun confirmExamRequest(
        @RequestHeader("Authorization") auth: String?,
        @RequestParam @NotBlank examRequestId: String?,
        @RequestParam @NotBlank professorId: String?,
    ): ResponseEntity<ConfirmExamResponse> = toOkResponseEntity(
            ConfirmExamResponse(adminService.confirmExamRequest(examRequestId, professorId))
        )
}