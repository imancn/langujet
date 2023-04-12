package com.cn.speaktest.exam.api

import com.cn.speaktest.advice.Message
import com.cn.speaktest.advice.toOkMessage
import com.cn.speaktest.exam.service.SuggestionService
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
        @PathVariable @NotBlank id: String?,
        @RequestParam grammar: String?,
        @RequestParam fluency: String?,
        @RequestParam vocabulary: String?,
        @RequestParam pronunciation: String?
    ): Message {
        return service.updateSuggestion(auth, id!!, grammar, fluency, vocabulary, pronunciation).toOkMessage()
    }
}