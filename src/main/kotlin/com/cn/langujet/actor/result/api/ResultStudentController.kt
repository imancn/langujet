package com.cn.langujet.actor.result.api

import com.cn.langujet.actor.result.payload.response.DetailedResultResponse
import com.cn.langujet.domain.result.service.ResultService
import jakarta.validation.constraints.NotBlank
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/v1/student/results")
@Validated
class ResultStudentController(
    private val resultService: ResultService
) {
    @GetMapping
    @PreAuthorize("hasRole('STUDENT')")
    fun getStudentResultsByExamSessionId(
        @RequestParam @NotBlank examSessionId: Long
    ): DetailedResultResponse {
        return resultService.getStudentDetailedResultByExamSessionId(examSessionId)
    }
}