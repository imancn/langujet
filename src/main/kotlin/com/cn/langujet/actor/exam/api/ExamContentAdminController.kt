package com.cn.langujet.actor.exam.api

import com.cn.langujet.actor.exam.payload.ExamContentDownloadLink
import com.cn.langujet.application.arch.controller.HistoricalEntityViewController
import com.cn.langujet.domain.exam.model.ExamContentEntity
import com.cn.langujet.domain.exam.service.ExamContentService
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.springframework.http.MediaType
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("api/v1/admin/exam-contents")
@Validated
class ExamContentAdminController(
    override var service: ExamContentService
) : HistoricalEntityViewController<ExamContentService, ExamContentEntity>() {
    @PostMapping("/exam-contents", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    @PreAuthorize("hasRole('ADMIN')")
    fun uploadExamContent(
        @RequestParam @NotNull examId: Long?,
        @RequestParam sectionId: Int?,
        @RequestParam partId: Int?,
        @RequestParam questionId: Int?,
        @RequestParam("file") file: MultipartFile
    ): ExamContentEntity {
        return service.uploadExamContent(file, examId!!, sectionId, partId, questionId)
    }
    
    @GetMapping("/exam-contents/download-links")
    @PreAuthorize("hasAnyRole('ADMIN')")
    fun getAdminExamContentDownloadLink(
        @RequestParam examId: @NotBlank Long?,
        @RequestParam sectionOrder: @NotNull Int?
    ): List<ExamContentDownloadLink> {
        return service.getAdminExamContentDownloadLink(
            examId!!, sectionOrder!!
        )
    }
}