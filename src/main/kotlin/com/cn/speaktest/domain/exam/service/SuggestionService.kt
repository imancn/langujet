package com.cn.speaktest.domain.exam.service

import com.cn.speaktest.application.advice.AccessDeniedException
import com.cn.speaktest.application.advice.NotFoundException
import com.cn.speaktest.application.security.security.model.Role
import com.cn.speaktest.domain.exam.model.Suggestion
import com.cn.speaktest.domain.exam.repository.SuggestionRepository
import com.cn.speaktest.domain.security.services.AuthService
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

    fun updateSuggestion(
        auth: String,
        suggestion: Suggestion
    ): Suggestion {
        return doWithPreAuth(auth, suggestion.id!!) {
            suggestionRepository.save(
                getSuggestionById(suggestion.id!!).also { suggestion ->
                    suggestion.overallRecommendation.let { suggestion.overallRecommendation = it }
                    suggestion.score.let { suggestion.score = it }
                    suggestion.issues.let { suggestion.issues = it }
                }
            )
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