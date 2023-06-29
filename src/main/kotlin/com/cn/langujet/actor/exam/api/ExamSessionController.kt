package com.cn.langujet.actor.exam.api

import com.cn.langujet.actor.exam.payload.dto.SuggestionDto
import com.cn.langujet.actor.util.toOkResponseEntity
import com.cn.langujet.domain.exam.model.*
import com.cn.langujet.domain.exam.service.ExamSessionServiceInterface
import com.cn.langujet.domain.professor.Professor
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/exam-session")
class ExamSessionController(private val examSessionService: ExamSessionServiceInterface) {

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/enroll")
    fun enrollExamSession(
        @RequestBody examRequest: ExamRequest,
        @RequestBody professor: Professor,
        @RequestBody exam: Exam
    ): ResponseEntity<ExamSession> = toOkResponseEntity(examSessionService.enrollExamSession(examRequest, professor))

    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/student")
    fun getStudentExamSession(
        @RequestHeader("Authorization") auth: String?,
        @RequestBody examSessionId: String
    ): ResponseEntity<ExamSession> {
        return toOkResponseEntity(examSessionService.getStudentExamSessionWithAuthToken(auth!!, examSessionId))
    }

    @PreAuthorize("hasRole('PROFESSOR')")
    @GetMapping("/professor")
    fun getProfessorExamSession(
        @RequestHeader("Authorization") auth: String?,
        @RequestBody examSessionId: String
    ): ResponseEntity<ExamSession> {
        return toOkResponseEntity(examSessionService.getProfessorExamSessionWithAuthToken(auth!!, examSessionId))
    }

    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/start")
    fun startExamSession(
        @RequestHeader("Authorization") auth: String?,
        @RequestParam examSessionId: String
    ): ResponseEntity<ExamIssue> {
        return toOkResponseEntity(examSessionService.startExamSession(auth!!, examSessionId))
    }

    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/next")
    fun nextExamIssue(
        @RequestHeader("Authorization") auth: String?,
        @RequestBody examSessionId: String,
        @RequestParam currentExamIssueOrder: Int
    ): ResponseEntity<ExamIssue> {
        return toOkResponseEntity(examSessionService.nextExamIssue(auth!!, examSessionId, currentExamIssueOrder))
    }

    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/finish")
    fun finishExamSession(
        @RequestHeader("Authorization") auth: String?,
        @RequestParam examSessionId: String
    ): ResponseEntity<ExamSession> {
        return toOkResponseEntity(examSessionService.finishExamSession(auth!!, examSessionId))
    }

    @PreAuthorize("hasAnyRole('PROFESSOR', 'ADMIN')")
    @PostMapping("/rate")
    fun rateExamSession(
        @RequestHeader("Authorization") auth: String?,
        @RequestBody examSessionId: String,
        @RequestBody suggestion: SuggestionDto
    ): ResponseEntity<ExamSession> {
        return toOkResponseEntity(examSessionService.rateExamSession(auth!!, examSessionId, suggestion))
    }
}