package com.cn.langujet.domain.exam.service

import com.cn.langujet.actor.exam.payload.dto.SuggestionDto
import com.cn.langujet.application.advice.AccessDeniedException
import com.cn.langujet.application.advice.InvalidTokenException
import com.cn.langujet.application.advice.MethodNotAllowedException
import com.cn.langujet.application.advice.NotFoundException
import com.cn.langujet.domain.security.services.AuthService
import com.cn.langujet.application.security.security.model.Role
import com.cn.langujet.domain.exam.model.*
import com.cn.langujet.domain.exam.repository.ExamSessionRepository
import com.cn.langujet.domain.professor.Professor
import org.springframework.stereotype.Service
import java.util.*

@Service
class ExamSessionService(
    val examSessionRepository: ExamSessionRepository,
    val examSectionService: ExamSectionService,
    val examRequestService: ExamRequestService,
    val examIssueService: ExamIssueService,
    val authService: AuthService,
) : ExamSessionServiceInterface {

    override fun enrollExamSession(examRequest: ExamRequest, professor: Professor, exam: Exam): ExamSession {
        var examSession = examSessionRepository.save(
            ExamSession(
                exam, examRequest.student, professor, examRequest.date
            )
        )
        examSession = examSessionRepository.save(
            examSession.also {
                it.examSections = initializeExamSections(examSession)
            }
        )
        examRequestService.deleteExamRequest(examRequest)
        return examSession
    }

    private fun initializeExamSections(examSession: ExamSession): List<ExamSection> {
        val examIssues = examIssueService.generateExamIssueList(examSession.id!!)
        return examSession.exam.sections.map { section ->
            ExamSection(
                null,
                examSession.id!!,
                examSession.exam,
                section,
                examIssues,
                null
            )
        }
    }

    override fun getStudentExamSessionWithAuthToken(
        authToken: String,
        examSessionId: String,
    ): ExamSession {
        val examSession = getExamSessionById(examSessionId)
        if (!authService.doesUserOwnsAuthToken(authToken, examSession.student.user.id))
            throw InvalidTokenException("Exam Session with id: $examSessionId is not belong to your token")
        return examSession
    }

    override fun getProfessorExamSessionWithAuthToken(
        authToken: String,
        examSessionId: String,
    ): ExamSession {
        val examSession = getExamSessionById(examSessionId)
        if (!authService.doesUserOwnsAuthToken(authToken, examSession.professor.user.id))
            throw InvalidTokenException("Exam Session with id: $examSessionId is not belong to your token")
        return examSession
    }

    override fun startExamSession(
        authToken: String,
        examSessionId: String
    ): ExamIssue {
        val examSession = getStudentExamSessionWithAuthToken(authToken, examSessionId)
        val examIssues = examSession.examSections?.first()?.examIssues
            ?: throw MethodNotAllowedException("ExamIssues for examSession with id: $examSessionId is null")

        if (examSession.isStarted)
            throw MethodNotAllowedException("Exam Session has been started")
        else {
            examSessionRepository.save(
                examSession.also {
                    val now = Date(System.currentTimeMillis())
                    it.startDate = now
                    it.endDate = Date(now.time + examSession.exam.examDuration)
                    it.isStarted = true
                }
            )
            return examIssues[0]
        }
    }

    override fun nextExamIssue(
        authToken: String,
        examSessionId: String,
        currentExamIssueOrder: Int
    ): ExamIssue {
        val examSection = examSectionService.getExamSectionById(examSessionId)
        val examSession = getExamSessionIfIsInProgress(authToken, examSection.examSessionId)
        val examIssues = getExamIssues(examSection)

        return if (examIssues.size <= currentExamIssueOrder + 1) {
            if (examSession.exam.sectionsNumber == examSection.section.order + 1)
                throw MethodNotAllowedException("There is no next Exam Issue")
            else
                getExamIssues(examSession.examSections?.get(examSection.section.order + 1))[0]
        } else
            examIssues[currentExamIssueOrder + 1]
    }

    private fun getExamIssues(examSection: ExamSection?): List<ExamIssue> {
        return examSection?.examIssues
            ?: throw MethodNotAllowedException("Exam Issues for Exam Session with id: $examSection.id")
    }

    override fun finishExamSession(
        authToken: String,
        examSessionId: String
    ): ExamSession {
        return getExamSessionIfIsInProgress(authToken, examSessionId).also { examSession ->
            if (!examSession.isFinished)
                examSessionRepository.save(
                    examSession.also {
                        it.isFinished = true
                    }
                )
        }

    }

    override fun rateExamSession(
        authToken: String,
        examSessionId: String,
        suggestion: SuggestionDto,
    ): ExamSession {
        val examSession = getExamSessionById(examSessionId)
        val user = authService.getUserByAuthToken(authToken)
        val doesUserOwnsAuthToken = authService.doesUserOwnsAuthToken(authToken, examSession.professor.user.id)
        val isAdmin = user.roles.contains(Role.ROLE_ADMIN)
        if (!doesUserOwnsAuthToken && !isAdmin)
            throw AccessDeniedException("Exam Session with id: $examSessionId is not belong to your token")
        else if (!examSession.isStarted)
            throw MethodNotAllowedException("The exam session has not been started")
        else if (!examSession.isFinished)
            throw MethodNotAllowedException("The exam session has not been finished")
        else if (examSession.isRated)
            throw MethodNotAllowedException("The exam session has been rated")
        else
            return examSessionRepository.save(
                examSession.also {
                    it.rateDate = Date(System.currentTimeMillis())
                    it.examSections?.find {
                            examSection -> examSection.id == suggestion.examSectionId
                    }?.suggestion = Suggestion(suggestion)
                    it.isRated = true
                }
            )
    }

    override fun isExamIssueAvailable(examIssueId: String): Boolean {
        val examSession = getExamSessionById(
            examSectionService.getExamSectionById(
                examIssueService.findById(examIssueId).examSectionId
            ).examSessionId
        )
        return !examSession.isFinished && examSession.isStarted
    }

    override fun getExamSessionBySuggestionId(suggestionId: String): ExamSession {
        return examSessionRepository.findBySuggestion_Id(suggestionId).orElseThrow {
            NotFoundException("ExamSession not found")
        }
    }

    private fun getExamSessionIfIsInProgress(authToken: String, examSessionId: String): ExamSession {
        val examSession = getStudentExamSessionWithAuthToken(authToken, examSessionId)
        if (!examSession.isStarted)
            throw MethodNotAllowedException("The exam session has not been started")
        else if (examSession.isFinished)
            throw MethodNotAllowedException("The exam session has been finished")
        else if (examSession.endDate?.time!! < System.currentTimeMillis()) {
            examSessionRepository.save(
                examSession.also {
                    it.isFinished = true
                }
            )
            throw MethodNotAllowedException("The exam session time is over")
        }
        return examSession
    }

    fun getExamSessionById(id: String?): ExamSession {
        return examSessionRepository.findById(id!!).orElseThrow {
            NotFoundException("ExamSession with id: $id not found")
        }
    }
}