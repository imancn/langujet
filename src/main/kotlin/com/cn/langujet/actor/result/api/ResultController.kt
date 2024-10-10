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
@RequestMapping("api/v1")
@Validated
class ResultController(
    private val resultService: ResultService, private val sectionResultService: SectionResultService
) {
    
    @GetMapping("/student/results")
    @PreAuthorize("hasRole('STUDENT')")
    fun getStudentResultsByExamSessionId(
        @RequestParam @NotBlank examSessionId: String
    ): DetailedResultResponse {
        return resultService.getStudentDetailedResultByExamSessionId(examSessionId)
    }
    
    @GetMapping("/corrector/results")
    @PreAuthorize("hasAnyRole('ADMIN', 'CORRECTOR')")
    fun getCorrectorDetailedResultByExamCorrectionId(
        @RequestParam @NotBlank examCorrectionId: String
    ): DetailedResultResponse {
        return resultService.getCorrectorDetailedResultByExamCorrectionId(examCorrectionId)
    }
    
    @PostMapping("/corrector/results/sections")
    @PreAuthorize("hasAnyRole('CORRECTOR_AI', 'CORRECTOR')")
    fun submitCorrectorSectionResult(
        @RequestBody submitCorrectorSectionResultRequest: SubmitCorrectorSectionResultRequest
    ) {
        return sectionResultService.submitCorrectorSectionResult(submitCorrectorSectionResultRequest)
    }
    
    @PostMapping("/corrector/results")
    @PreAuthorize("hasAnyRole('CORRECTOR_AI', 'CORRECTOR')")
    fun submitCorrectorResult(
        @RequestBody submitCorrectorResultRequest: SubmitCorrectorResultRequest
    ) {
        return resultService.submitCorrectorResult(submitCorrectorResultRequest)
    }
    
    @PostMapping("/corrector/results/sections/attachment", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    @PreAuthorize("hasAnyRole('CORRECTOR_AI', 'CORRECTOR')")
    fun attachCorrectorSectionResultFile(
        @RequestParam attachment: MultipartFile,
        @RequestParam sectionCorrectionId: String,
    ) {
        return sectionResultService.attachCorrectorSectionResultFile(attachment, sectionCorrectionId)
    }
}