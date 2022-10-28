package com.cn.speaktest.controller

import com.cn.speaktest.model.Professor
import com.cn.speaktest.repository.user.ProfessorRepository
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@CrossOrigin(origins = ["*"], maxAge = 3600)
@RestController
@RequestMapping("/api/admin")
class AdminController(
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
}