package com.cn.langujet.actor.exam.api

import com.cn.langujet.application.advice.Message
import com.cn.langujet.application.advice.toOkMessage
import com.cn.langujet.domain.exam.model.Suggestion
import com.cn.langujet.domain.exam.service.SuggestionService
import jakarta.validation.constraints.NotBlank
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
    ): Message {
        return service.getSuggestionsByExamSessionId(id!!).toOkMessage()
    }

    @GetMapping("/exam-section/{id}")
    fun getSuggestionByExamSectionId(
        @PathVariable @NotBlank id: String?
    ): Message {
        return service.getSuggestionByExamSectionId(id!!).toOkMessage()
    }

    @GetMapping("/{id}")
    fun getSuggestionById(
        @PathVariable @NotBlank id: String?
    ): Message {
        return service.getSuggestionById(id!!).toOkMessage()
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','PROFESSOR') ")
    fun updateSuggestion(
        @RequestHeader("Authorization") auth: String?,
        @RequestBody suggestion: Suggestion
    ): Message {
        return service.updateSuggestion(auth!!, suggestion).toOkMessage()
    }
}