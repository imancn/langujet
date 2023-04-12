package com.cn.speaktest.exam.api

import com.cn.speaktest.exam.model.ExamRequest
import com.cn.speaktest.exam.api.request.RateRequest
import com.cn.speaktest.exam.model.Exam
import com.cn.speaktest.exam.model.ExamIssue
import com.cn.speaktest.exam.model.ExamSession
import com.cn.speaktest.exam.service.ExamSessionServiceInterface
import com.cn.speaktest.professor.Professor
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
    ): ExamSession {
        return examSessionService.enrollExamSession(examRequest, professor, exam)
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
        @RequestBody examSessionId: String
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
        @RequestBody examSessionId: String
    ): ExamSession {
        return examSessionService.finishExamSession(auth!!, examSessionId)
    }

    @PreAuthorize("hasAnyRole('PROFESSOR', 'ADMIN')")
    @PostMapping("/rate")
    fun rateExamSession(
        @RequestHeader("Authorization") auth: String?,
        @RequestBody examSessionId: String,
        @RequestBody rate: RateRequest
    ): ExamSession {
        return examSessionService.rateExamSession(
            auth!!,
            examSessionId,
            rate.score,
            rate.suggestion
        )
    }
}