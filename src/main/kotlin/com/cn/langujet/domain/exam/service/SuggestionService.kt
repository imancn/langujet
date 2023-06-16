package com.cn.langujet.domain.exam.service

import com.cn.langujet.application.advice.AccessDeniedException
import com.cn.langujet.application.advice.NotFoundException
import com.cn.langujet.application.security.security.model.Role
import com.cn.langujet.domain.exam.model.Suggestion
import com.cn.langujet.domain.exam.repository.SuggestionRepository
import com.cn.langujet.domain.security.services.AuthService
import org.springframework.stereotype.Service

@Service
class SuggestionService(
    private val suggestionRepository: SuggestionRepository,
    private val examSessionService: ExamSessionService,
    private val examSectionService: ExamSectionService,
    val authService: AuthService
) {
    fun getSuggestionById(id: String): Suggestion {
        return suggestionRepository.findById(id)
            .orElseThrow { throw NotFoundException("Suggestion with ID: $id not found") }
    }

    fun getSuggestionsByExamSessionId(examSessionId: String): List<Suggestion> {
        val examSections = examSessionService.getExamSessionById(examSessionId).examSections
        return examSections?.mapNotNull { it.suggestion }
            ?: throw NotFoundException("Suggestions for ExamSession with ID: $examSessionId not found")
    }

    fun getSuggestionByExamSectionId(examSectionId: String): Suggestion {
        return examSectionService.getExamSectionById(examSectionId).suggestion
            ?: throw NotFoundException("Suggestion for ExamSection with ID: $examSectionId not found")
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
                    suggestion.recommendations.let { suggestion.recommendations = it }
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