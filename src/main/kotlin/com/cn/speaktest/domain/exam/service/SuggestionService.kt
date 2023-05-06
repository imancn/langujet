package com.cn.speaktest.domain.exam.service

import com.cn.speaktest.application.advice.AccessDeniedException
import com.cn.speaktest.application.advice.NotFoundException
import com.cn.speaktest.domain.security.services.AuthService
import com.cn.speaktest.application.security.security.model.Role
import com.cn.speaktest.domain.exam.model.Suggestion
import com.cn.speaktest.domain.exam.repository.SuggestionRepository
import org.springframework.stereotype.Service

@Service
class SuggestionService(
    private val suggestionRepository: SuggestionRepository,
    private val examSessionService: ExamSessionService,
    val authService: AuthService
) {
    fun getSuggestionById(id: String): Suggestion {
        return suggestionRepository.findById(id)
            .orElseThrow { throw NotFoundException("Suggestion with ID: $id not found") }
    }

    fun getSuggestionByExamSessionId(examSessionId: String): Suggestion {
        return examSessionService.getExamSessionById(examSessionId).suggestion
            ?: throw NotFoundException("Suggestion for ExamSession with ID: $examSessionId not found")
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    fun updateSuggestion(
        auth: String?,
        id: String,
        grammar: String?,
        fluency: String?,
        vocabulary: String?,
        pronunciation: String?,
    ): Suggestion {
        return doWithPreAuth(
            auth!!, id
        ) {
            suggestionRepository.save(getSuggestionById(id).also { suggestion ->
                grammar?.let { suggestion.grammar = it }
                fluency?.let { suggestion.fluency = it }
                vocabulary?.let { suggestion.vocabulary = it }
                pronunciation?.let { suggestion.pronunciation = it }
            })
        }
    }

    fun doWithPreAuth(auth: String, suggestionId: String, function: () -> Suggestion): Suggestion {
        val examSession = examSessionService.getExamSessionBySuggestionId(suggestionId)
        val doesUserOwnsAuthToken = authService.doesUserOwnsAuthToken(auth, examSession.professor.user.id)
        val isAdmin = authService.getUserByAuthToken(auth).roles.contains(Role.ROLE_ADMIN)
        return if (!doesUserOwnsAuthToken && !isAdmin)
            throw AccessDeniedException("Exam Session with id: ${examSession.id} is not belong to your token")
        else function()
    }
}