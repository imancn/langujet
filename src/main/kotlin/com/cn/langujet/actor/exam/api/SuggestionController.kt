package com.cn.langujet.actor.exam.api

import com.cn.langujet.actor.util.toOkResponseEntity
import com.cn.langujet.domain.exam.model.Suggestion
import com.cn.langujet.domain.exam.service.SuggestionService
import jakarta.validation.constraints.NotBlank
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/suggestions")
class SuggestionController(
    private var service: SuggestionService
) {
    @GetMapping("/exam-session/{id}")
    fun getSuggestionsByExamSessionId(
        @PathVariable @NotBlank id: String?
    ): ResponseEntity<List<Suggestion>> = toOkResponseEntity(service.getSuggestionsByExamSessionId(id!!))

    @GetMapping("/exam-section/{id}")
    fun getSuggestionByExamSectionId(
        @PathVariable @NotBlank id: String?
    ): ResponseEntity<Suggestion> = toOkResponseEntity(service.getSuggestionByExamSectionId(id!!))

    @GetMapping("/{id}")
    fun getSuggestionById(
        @PathVariable @NotBlank id: String?
    ): ResponseEntity<Suggestion> = toOkResponseEntity(service.getSuggestionById(id!!))

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','PROFESSOR') ")
    fun updateSuggestion(
        @RequestHeader("Authorization") auth: String?,
        @RequestBody suggestion: Suggestion
    ): ResponseEntity<Suggestion> = toOkResponseEntity(service.updateSuggestion(auth!!, suggestion))
}