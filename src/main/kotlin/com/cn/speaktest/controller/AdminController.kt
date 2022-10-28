package com.cn.speaktest.controller

import com.cn.speaktest.model.ExamRequest
import com.cn.speaktest.model.Professor
import com.cn.speaktest.repository.exam.ExamRequestRepository
import com.cn.speaktest.repository.user.ProfessorRepository
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@CrossOrigin(origins = ["*"], maxAge = 3600)
@RestController
@RequestMapping("/api/admin")
class AdminController(
    val examRequestRepository: ExamRequestRepository,
    val professorRepository: ProfessorRepository,
) {
    @GetMapping("/get-professors")
    @PreAuthorize("hasRole('ADMIN')")
    fun getProfessors(@RequestHeader("Authorization") auth: String): ResponseEntity<List<Professor>> {
        return ResponseEntity.ok(
            professorRepository.findAll().sortedBy {
                it.fullName
            }
        )
    }

    @GetMapping("/get-exam-requests")
    @PreAuthorize("hasRole('ADMIN')")
    fun getExamRequests(@RequestHeader("Authorization") auth: String): ResponseEntity<List<ExamRequest>> {
        return ResponseEntity.ok(
            examRequestRepository.findAll().sortedByDescending {
                it.date
            }
        )
    }
}