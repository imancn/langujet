package com.cn.langujet.actor.result.api

import com.cn.langujet.actor.result.payload.request.SubmitCorrectorResultRequest
import com.cn.langujet.actor.result.payload.request.SubmitCorrectorSectionResultRequest
import com.cn.langujet.actor.result.payload.response.DetailedResultResponse
import com.cn.langujet.domain.result.service.ResultService
import com.cn.langujet.domain.result.service.SectionResultService
import jakarta.validation.constraints.NotBlank
import org.springframework.http.MediaType
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("api/v1/corrector/results")
@Validated
class ResultCorrectorController(
    private val resultService: ResultService, private val sectionResultService: SectionResultService
) {
    @GetMapping
    @PreAuthorize("hasAnyRole('CORRECTOR')")
    fun getCorrectorDetailedResultByExamCorrectionId(
        @RequestParam @NotBlank examCorrectionId: Long
    ): DetailedResultResponse {
        return resultService.getCorrectorDetailedResultByExamCorrectionId(examCorrectionId)
    }
    
    @PostMapping("/sections")
    @PreAuthorize("hasAnyRole('CORRECTOR_AI', 'CORRECTOR')")
    fun submitCorrectorSectionResult(
        @RequestBody submitCorrectorSectionResultRequest: SubmitCorrectorSectionResultRequest
    ) {
        return sectionResultService.submitCorrectorSectionResult(submitCorrectorSectionResultRequest)
    }
    
    @PostMapping
    @PreAuthorize("hasAnyRole('CORRECTOR_AI', 'CORRECTOR')")
    fun submitCorrectorResult(
        @RequestBody submitCorrectorResultRequest: SubmitCorrectorResultRequest
    ) {
        return resultService.submitCorrectorResult(submitCorrectorResultRequest)
    }
    
    @PostMapping("/sections/attachment", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    @PreAuthorize("hasAnyRole('CORRECTOR_AI', 'CORRECTOR')")
    fun attachCorrectorSectionResultFile(
        @RequestParam attachment: MultipartFile,
        @RequestParam sectionCorrectionId: Long,
    ) {
        return sectionResultService.attachCorrectorSectionResultFile(attachment, sectionCorrectionId)
    }
}