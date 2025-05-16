package com.cn.langujet.actor.exam.api

import com.cn.langujet.actor.exam.payload.ExamContentDownloadLink
import com.cn.langujet.domain.exam.service.ExamContentService
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/v1/student/exam-contents")
@Validated
class ExamContentStudentController(
    private val examContentService: ExamContentService
) {
    @GetMapping("/exam-contents/download-links")
    @PreAuthorize("hasAnyRole('STUDENT')")
    fun getStudentExamContentDownloadLink(
        @RequestParam examSessionId: @NotBlank Long?,
        @RequestParam sectionOrder: @NotNull Int? /// todo
    ): List<ExamContentDownloadLink> {
        return examContentService.getStudentExamContentDownloadLink(
            examSessionId!!, sectionOrder!!
        )
    }
}