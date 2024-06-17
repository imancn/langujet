package com.cn.langujet.actor.result.api

import com.cn.langujet.actor.result.payload.response.DetailedResultResponse
import com.cn.langujet.actor.result.payload.request.AddCorrectorSectionResultRequest
import com.cn.langujet.domain.result.service.ResultService
import jakarta.validation.constraints.NotBlank
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/v1")
@Validated
class ResultController(
    private var service: ResultService
) {
    
    @GetMapping("/student/results")
    @PreAuthorize("hasRole('STUDENT')")
    fun getStudentResultsByExamSessionId(
        @RequestParam @NotBlank examSessionId: String
    ): DetailedResultResponse {
        return service.getDetailedResultByExamSessionId(examSessionId)
    }
    
    @PostMapping("/corrector/results/sections")
    @PreAuthorize("hasRole('CORRECTOR')")
    fun addCorrectorSectionResult(
        @RequestBody addCorrectorSectionResultRequest: AddCorrectorSectionResultRequest
    ) {
        return service.addCorrectorSectionResult(addCorrectorSectionResultRequest)
    }
}