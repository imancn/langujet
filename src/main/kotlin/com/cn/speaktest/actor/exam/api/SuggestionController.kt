package com.cn.speaktest.actor.exam.api

import com.cn.speaktest.application.advice.Message
import com.cn.speaktest.application.advice.toOkMessage
import com.cn.speaktest.domain.exam.model.Suggestion
import com.cn.speaktest.domain.exam.service.SuggestionService
import jakarta.validation.constraints.NotBlank
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/suggestions")
class SuggestionController(
    private var service: SuggestionService
) {
    @GetMapping("/exam-session/{id}")
    fun getSuggestionByExamSessionId(
        @PathVariable @NotBlank id: String?
    ): Message {
        return service.getSuggestionByExamSessionId(id!!).toOkMessage()
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