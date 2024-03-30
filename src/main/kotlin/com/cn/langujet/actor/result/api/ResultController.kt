package com.cn.langujet.actor.result.api

import com.cn.langujet.actor.result.payload.DetailedResultResponse
import com.cn.langujet.domain.result.service.ResultService
import jakarta.validation.constraints.NotBlank
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/v1")
@Validated
class ResultController(
    private var service: ResultService
) {
    
    @GetMapping("student/result/exam-session/{id}")
    fun getStudentResultsByExamSessionId(
        @RequestHeader("Authorization") auth: String,
        @PathVariable @NotBlank id: String
    ): DetailedResultResponse {
        return service.getDetailedResultByExamSessionId(auth, id)
    }
}