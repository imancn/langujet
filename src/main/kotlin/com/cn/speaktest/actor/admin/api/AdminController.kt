package com.cn.speaktest.actor.admin.api

import com.cn.speaktest.actor.admin.payload.response.ConfirmExamResponse
import com.cn.speaktest.application.advice.Message
import com.cn.speaktest.application.advice.toOkMessage
import com.cn.speaktest.domain.admin.AdminService
import jakarta.validation.constraints.NotBlank
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
    fun getProfessors(@RequestHeader("Authorization") auth: String?): Message {
        return adminService.getProfessors().toOkMessage()
    }

    @GetMapping("/get-exam-requests")
    @PreAuthorize("hasRole('ADMIN')")
    fun getExamRequests(@RequestHeader("Authorization") auth: String?): Message {
        return adminService.getExamRequests().toOkMessage()
    }

    @PostMapping("/confirm-exam-request")
    @PreAuthorize("hasRole('ADMIN')")
    fun confirmExamRequest(
        @RequestHeader("Authorization") auth: String?,
        @RequestParam @NotBlank examRequestId: String?,
        @RequestParam @NotBlank professorId: String?,
    ): Message {
        return Message(
            ConfirmExamResponse(
                adminService.confirmExamRequest(examRequestId, professorId)
            ),
            "Exam have been confirmed"
        )
    }
}