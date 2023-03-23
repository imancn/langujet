package com.cn.speaktest.admin.api

import com.cn.speaktest.admin.AdminService
import com.cn.speaktest.admin.payload.request.AddQuestionRequest
import com.cn.speaktest.admin.payload.response.ConfirmExamResponse
import com.cn.speaktest.advice.Message
import com.cn.speaktest.advice.toOkMessage
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
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

    @PostMapping("/confirm-exam")
    @PreAuthorize("hasRole('ADMIN')")
    fun confirmExam(
        @RequestHeader("Authorization") auth: String?,
        @RequestParam @NotBlank examRequestId: String?,
        @RequestParam @NotBlank professorId: String?,
    ): Message {
        return Message(
            ConfirmExamResponse(
                adminService.confirmExam(examRequestId, professorId)
            ),
            "Exam have been confirmed"
        )
    }

    @PostMapping("/add-question")
    @PreAuthorize("hasRole('ADMIN')")
    fun addQuestion(
        @RequestHeader("Authorization") auth: String?,
        @Valid @NotNull @RequestBody addQuestionRequest: AddQuestionRequest?
    ): Message {
        val question = adminService.addQuestion(addQuestionRequest)
        return Message(question, "Question added successfully")
    }
}