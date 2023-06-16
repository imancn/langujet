package com.cn.langujet.actor.exam.api

import com.cn.langujet.actor.util.toOkResponseEntity
import com.cn.langujet.domain.exam.model.ExamRequest
import com.cn.langujet.domain.exam.service.ExamRequestService
import jakarta.validation.constraints.NotBlank
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@Validated
@RequestMapping("/exam-requests")
class ExamRequestController(private val examRequestService: ExamRequestService) {
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    fun createExamRequest(
        @RequestParam @NotBlank studentId: String?,
        @RequestParam @NotBlank examId: String?
    ): ResponseEntity<ExamRequest> =
        toOkResponseEntity(examRequestService.createExamRequest(studentId, examId))

    @GetMapping("/by-exam-id/{examId}")
    @PreAuthorize("hasAnyRole('ADMIN','STUDENT') ")
    fun getExamRequestsByExamId(@PathVariable examId: String): ResponseEntity<List<ExamRequest>> =
        toOkResponseEntity(examRequestService.getExamRequestsByExamId(examId))

    @GetMapping("/by-student-id/{studentId}")
    @PreAuthorize("hasAnyRole('ADMIN','STUDENT') ")
    fun getExamRequestsByStudentId(@PathVariable studentId: String): ResponseEntity<List<ExamRequest>> =
        toOkResponseEntity(examRequestService.getExamRequestsByStudentId(studentId))
}