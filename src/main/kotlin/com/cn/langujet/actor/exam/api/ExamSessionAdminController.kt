package com.cn.langujet.actor.exam.api

import com.cn.langujet.actor.exam.payload.ExamSessionEnrollResponse
import com.cn.langujet.application.arch.controller.HistoricalEntityViewController
import com.cn.langujet.domain.exam.model.ExamSessionEntity
import com.cn.langujet.domain.exam.service.ExamSessionService
import jakarta.validation.constraints.NotBlank
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/admin/exam-sessions")
@Validated
class ExamSessionAdminController(
    private val examSessionService: ExamSessionService, override var service: ExamSessionService
) : HistoricalEntityViewController<ExamSessionService, ExamSessionEntity>() {
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/enroll")
    fun enrollExamSession(
        @RequestParam @NotBlank email: String,
        @RequestParam @NotBlank examServiceId: Long,
        @RequestParam examId: Long?,
    ): ExamSessionEnrollResponse = examSessionService.enrollExamSessionByEmail(email, examServiceId, examId)
}