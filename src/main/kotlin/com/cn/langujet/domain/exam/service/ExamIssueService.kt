package com.cn.langujet.domain.exam.service

import com.cn.langujet.application.advice.AccessDeniedException
import com.cn.langujet.application.advice.NotFoundException
import com.cn.langujet.domain.security.services.AuthService
import com.cn.langujet.application.security.security.model.Role
import com.cn.langujet.domain.answer.model.Answer
import com.cn.langujet.domain.exam.model.ExamIssue
import com.cn.langujet.domain.exam.model.Section
import com.cn.langujet.domain.exam.repository.ExamIssueRepository
import com.cn.langujet.domain.question.QuestionService
import com.cn.langujet.domain.question.model.Question
import org.springframework.stereotype.Service

@Service
class ExamIssueService(
    private val examIssueRepository: ExamIssueRepository,
    private val questionService: QuestionService,
    private val examSectionService: ExamSectionService,
    private val authService: AuthService
) {
    private lateinit var examSessionService: ExamSessionService

    fun findById(id: String): ExamIssue {
        return examIssueRepository.findById(id)
            .orElseThrow { NotFoundException("ExamIssue with id: $id not found") }
    }

    fun findByExamSectionId(auth: String, id: String): List<ExamIssue> {
        val examIssues = examSectionService.getExamSectionById(id).examIssues
        examIssues.forEach {
            preAuthCheck(auth, it.id!!)
        }
        return examIssues
    }

    fun findByAnswerId(auth: String, answerId: String): ExamIssue {
        val examIssue = examIssueRepository.findByAnswerId(answerId)
            ?: throw NotFoundException("ExamIssue with Answer id: $answerId not found")

        preAuthCheck(auth, examIssue.id!!)
        return examIssue
    }

    fun preAuthCheck(auth: String, examIssueId: String): Boolean {
        val examIssue = findById(examIssueId)
        val examSession = examSessionService.getExamSessionById(examIssue.examSectionId)
        val doesStudentOwnsAuthToken = authService.doesUserOwnsAuthToken(auth, examSession.student.id)
        val doesProfessorOwnsAuthToken = authService.doesUserOwnsAuthToken(auth, examSession.professor.id)
        val isAdmin = authService.getUserByAuthToken(auth).roles.contains(Role.ROLE_ADMIN)
        if (!doesStudentOwnsAuthToken && !doesProfessorOwnsAuthToken && !isAdmin)
            throw AccessDeniedException("Exam Issue with id: $examIssueId is not belong to your token")
        return true
    }

    fun create(examIssue: ExamIssue): ExamIssue {
        return examIssueRepository.save(examIssue)
    }

    fun generateExamIssueList(section: Section): List<ExamIssue> {
        val examIssues = mutableListOf<ExamIssue>().toMutableList()
        val usedQuestions = mutableListOf<Question>().toMutableList()
        val topic = questionService.getAllQuestionsBySection(section).random().topic
        val questions = questionService.getAllQuestionsByTopic(topic).sortedBy { it.order }
        questions.forEach { question ->
            examIssues.add(
                create(
                    ExamIssue(
                        section.id!!, question, examIssues.size
                    )
                )
            )
        }
        usedQuestions.addAll(questions)
        questionService.updateQuestions(
            usedQuestions.onEach { it.usageNumber = it.usageNumber + 1 }
        )
        return examIssues
    }

    fun setAnswer(id: String, answer: Answer): ExamIssue {
        return examIssueRepository.save(
            findById(id).also {
                it.answer = answer
            }
        )
    }
}