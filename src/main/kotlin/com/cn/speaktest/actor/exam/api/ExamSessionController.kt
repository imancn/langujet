package com.cn.speaktest.actor.exam.api

import com.cn.speaktest.actor.exam.payload.dto.SuggestionDto
import com.cn.speaktest.domain.exam.model.*
import com.cn.speaktest.domain.exam.service.ExamSessionServiceInterface
import com.cn.speaktest.domain.professor.Professor
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
        @RequestBody examMeta: ExamMeta
    ): ExamSession {
        return examSessionService.enrollExamSession(examRequest, professor, examMeta)
    }

    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/student")
    fun getStudentExamSession(
        @RequestHeader("Authorization") auth: String?,
        @RequestBody examSessionId: String
    ): ExamSession {
        return examSessionService.getStudentExamSessionWithAuthToken(auth!!, examSessionId)
    }

    @PreAuthorize("hasRole('PROFESSOR')")
    @GetMapping("/professor")
    fun getProfessorExamSession(
        @RequestHeader("Authorization") auth: String?,
        @RequestBody examSessionId: String
    ): ExamSession {
        return examSessionService.getProfessorExamSessionWithAuthToken(auth!!, examSessionId)
    }

    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/start")
    fun startExamSession(
        @RequestHeader("Authorization") auth: String?,
        @RequestParam examSessionId: String
    ): ExamIssue {
        return examSessionService.startExamSession(auth!!, examSessionId)
    }

    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/next")
    fun nextExamIssue(
        @RequestHeader("Authorization") auth: String?,
        @RequestBody examSessionId: String,
        @RequestParam currentExamIssueOrder: Int
    ): ExamIssue {
        return examSessionService.nextExamIssue(auth!!, examSessionId, currentExamIssueOrder)
    }

    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/finish")
    fun finishExamSession(
        @RequestHeader("Authorization") auth: String?,
        @RequestParam examSessionId: String
    ): ExamSession {
        return examSessionService.finishExamSession(auth!!, examSessionId)
    }

    @PreAuthorize("hasAnyRole('PROFESSOR', 'ADMIN')")
    @PostMapping("/rate")
    fun rateExamSession(
        @RequestHeader("Authorization") auth: String?,
        @RequestBody examSessionId: String,
        @RequestBody suggestion: SuggestionDto
    ): ExamSession {
        return examSessionService.rateExamSession(auth!!, examSessionId, suggestion)
    }
}