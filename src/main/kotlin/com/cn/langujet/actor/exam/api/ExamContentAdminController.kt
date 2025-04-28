package com.cn.langujet.actor.exam.api

import com.cn.langujet.actor.exam.payload.ExamContentDownloadLink
import com.cn.langujet.application.arch.controller.HistoricalEntityViewController
import com.cn.langujet.domain.exam.model.ExamContentEntity
import com.cn.langujet.domain.exam.repository.ExamContentRepository
import com.cn.langujet.domain.exam.service.ExamContentService
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("api/v1/admin/exam-contents")
@Validated
class ExamContentAdminController(
    private val examContentService: ExamContentService
) : HistoricalEntityViewController<ExamContentRepository, ExamContentService, ExamContentEntity>() {
    @PostMapping("/exam-contents")
    @PreAuthorize("hasRole('ADMIN')")
    fun uploadExamContent(
        @RequestParam @NotBlank examId: Long?,
        @RequestParam @NotNull sectionOrder: Int?,
        @RequestParam @NotNull partOrder: Int?,
        @RequestParam @NotNull questionOrder: Int?,
        @RequestParam("file") file: MultipartFile
    ): ExamContentEntity {
        return examContentService.uploadExamContent(file, examId!!, sectionOrder, partOrder, questionOrder)
    }
    
    @GetMapping("/exam-contents/download-links")
    @PreAuthorize("hasAnyRole('ADMIN')")
    fun getAdminExamContentDownloadLink(
        @RequestParam examId: @NotBlank Long?,
        @RequestParam sectionOrder: @NotNull Int?
    ): List<ExamContentDownloadLink> {
        return examContentService.getAdminExamContentDownloadLink(
            examId!!, sectionOrder!!
        )
    }
}