package com.cn.langujet.actor.exam.api

import com.cn.langujet.actor.exam.payload.ExamSectionContentDownloadLink
import com.cn.langujet.domain.exam.model.ExamSectionContent
import com.cn.langujet.domain.exam.service.ExamSectionContentService
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("api/v1")
@Validated
class ExamSectionContentController(
    private val examSectionContentService: ExamSectionContentService
) {
    @PostMapping("admin/exam/section/content")
    @PreAuthorize("hasRole('ADMIN')")
    fun uploadExamSectionContent(
        @RequestParam @NotBlank examId: String?,
        @RequestParam @NotNull sectionOrder: Int?,
        @RequestParam("file") file: MultipartFile
    ): ExamSectionContent {
        return examSectionContentService.uploadExamSectionContent(examId!!, sectionOrder!!, file)
    }

    @GetMapping("/admin/exam/section/content/download-links")
    @PreAuthorize("hasAnyRole('ADMIN')")
    fun getAdminExamSectionContentDownloadLink(
        @RequestParam examId: @NotBlank String?,
        @RequestParam sectionOrder: @NotNull Int?
    ): List<ExamSectionContentDownloadLink> {
        return examSectionContentService.getAdminExamSectionContentDownloadLink(
            examId!!, sectionOrder!!
        )
    }

    @GetMapping("/student/exam/section/content/download-links")
    @PreAuthorize("hasAnyRole('STUDENT')")
    fun getStudentExamSectionContentDownloadLink(
        @RequestParam examSessionId: @NotBlank String?,
        @RequestParam sectionOrder: @NotNull Int?
    ): List<ExamSectionContentDownloadLink> {
        return examSectionContentService.getStudentExamSectionContentDownloadLink(
            examSessionId!!, sectionOrder!!
        )
    }
}