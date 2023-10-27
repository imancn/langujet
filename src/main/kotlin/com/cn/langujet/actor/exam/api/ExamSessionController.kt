package com.cn.langujet.actor.exam.api

import com.cn.langujet.actor.exam.payload.ExamRequest
import com.cn.langujet.actor.exam.payload.SectionDTO
import com.cn.langujet.domain.exam.model.*
import com.cn.langujet.domain.exam.service.ExamSessionService
import jakarta.validation.constraints.NotBlank
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/exam-session")
class ExamSessionController(
    private val examSessionService: ExamSessionService
){

    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/enroll")
    fun enrollExamSession(
        @RequestHeader("Authorization") @NotBlank auth: String?,
        @RequestBody examRequest: ExamRequest,
    ): ResponseEntity<ExamSession> =
        ResponseEntity.ok(examSessionService.enrollExamSession(auth!!, examRequest))

    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/student/{examSessionId}")
    fun getStudentExamSession(
        @RequestHeader("Authorization") @NotBlank auth: String?,
        @PathVariable @NotBlank examSessionId: String?
    ): ResponseEntity<ExamSession> =
        ResponseEntity.ok(examSessionService.getStudentExamSession(auth!!, examSessionId!!))

    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/student-sessions")
    fun getStudentExamSessions(
        @RequestHeader("Authorization") @NotBlank auth: String?
    ): ResponseEntity<List<ExamSession>> =
        ResponseEntity.ok(examSessionService.getStudentExamSessions(auth!!))

    @PreAuthorize("hasRole('PROFESSOR')")
    @GetMapping("/professor/{examSessionId}")
    fun getProfessorExamSession(
        @RequestHeader("Authorization") @NotBlank auth: String?,
        @PathVariable @NotBlank examSessionId: String?
    ): ResponseEntity<ExamSession> =
        ResponseEntity.ok(examSessionService.getProfessorExamSession(auth!!, examSessionId!!))

    @PreAuthorize("hasRole('PROFESSOR')")
    @GetMapping("/professor-sessions")
    fun getProfessorExamSessions(
        @RequestHeader("Authorization") @NotBlank auth: String?
    ): ResponseEntity<List<ExamSession>> =
        ResponseEntity.ok(examSessionService.getProfessorExamSessions(auth!!))

    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/start")
    fun startExamSession(
        @RequestHeader("Authorization") @NotBlank auth: String?,
        @RequestParam @NotBlank examSessionId: String?
    ): ResponseEntity<SectionDTO> =
        ResponseEntity.ok(examSessionService.startExamSession(auth!!, examSessionId!!))

    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/next")
    fun nextExamIssue(
        @RequestHeader("Authorization") @NotBlank auth: String?,
        @RequestParam @NotBlank examSessionId: String?,
        @RequestParam @NotBlank currentExamIssueOrder: Int?
    ): ResponseEntity<SectionDTO> =
        ResponseEntity.ok(examSessionService.nextExamSection(auth!!, examSessionId!!, currentExamIssueOrder!!))

    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/finish")
    fun finishExamSession(
        @RequestHeader("Authorization") @NotBlank auth: String?,
        @RequestParam @NotBlank examSessionId: String?
    ): ResponseEntity<ExamSession> =
        ResponseEntity.ok(examSessionService.finishExamSession(auth!!, examSessionId!!))
}