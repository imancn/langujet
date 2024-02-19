package com.cn.langujet.actor.exam.api

import com.cn.langujet.actor.exam.payload.ResultDto
import com.cn.langujet.actor.util.toOkResponseEntity
import com.cn.langujet.domain.exam.service.ResultService
import jakarta.validation.constraints.NotBlank
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/v1")
@Validated
class ResultController(
    private var service: ResultService
) {
    @PostMapping("/result")
    @PreAuthorize("hasAnyRole('PROFESSOR', 'ADMIN')")
    fun createResult(
        @RequestHeader("Authorization") auth: String?,
        @RequestBody examSessionId: String,
        @RequestBody result: ResultDto
    ): ResponseEntity<ResultDto> {
        return toOkResponseEntity(service.createResult(auth!!, examSessionId, result))
    }
    @GetMapping("student/result/exam-session/{id}")
    fun getStudentResultsByExamSessionId(
        @RequestHeader("Authorization") auth: String?,
        @PathVariable @NotBlank id: String?
    ): ResponseEntity<ResultDto> = toOkResponseEntity(service.getResultsByExamSessionId(auth!!, id!!))

    @PostMapping("/result/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','PROFESSOR') ")
    fun updateResult(
        @RequestHeader("Authorization") auth: String?,
        @RequestBody result: ResultDto,
        @PathVariable @NotBlank id: String?
    ): ResponseEntity<ResultDto> = toOkResponseEntity(service.updateResult(auth!!, result, id!!))
}