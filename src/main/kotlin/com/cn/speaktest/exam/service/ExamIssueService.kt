package com.cn.speaktest.exam.service

import com.cn.speaktest.advice.AccessDeniedException
import com.cn.speaktest.advice.NotFoundException
import com.cn.speaktest.answer.model.Answer
import com.cn.speaktest.exam.model.ExamIssue
import com.cn.speaktest.question.model.Question
import com.cn.speaktest.exam.repository.ExamIssueRepository
import com.cn.speaktest.question.QuestionService
import com.cn.speaktest.security.api.AuthService
import com.cn.speaktest.security.model.Role
import org.springframework.stereotype.Service

@Service
class ExamIssueService(
    private val examIssueRepository: ExamIssueRepository,
    private val questionService: QuestionService,
    private val examService: ExamService,
    private val examSessionService: ExamSessionService,
    private val authService: AuthService
) {
    fun findById(id: String): ExamIssue {
        return examIssueRepository.findById(id)
            .orElseThrow { NotFoundException("ExamIssue with id: $id not found") }
    }

    fun findByExamSessionId(auth: String, id: String): List<ExamIssue> {
        val examIssues = examIssueRepository.findByExamSessionId(id)
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

    fun preAuthCheck(auth: String, examIssueId: String) {
        val examIssue = findById(examIssueId)
        val examSession = examSessionService.getExamSessionById(examIssue.examSessionId)
        val doesStudentOwnsAuthToken = authService.doesUserOwnsAuthToken(auth, examSession.student.id)
        val doesProfessorOwnsAuthToken = authService.doesUserOwnsAuthToken(auth, examSession.professor.id)
        val isAdmin = authService.getUserByAuthToken(auth).roles.contains(Role.ROLE_ADMIN)
        if (!doesStudentOwnsAuthToken && !doesProfessorOwnsAuthToken && !isAdmin)
            throw AccessDeniedException("Exam Issue with id: $examIssueId is not belong to your token")
    }

    fun create(examIssue: ExamIssue): ExamIssue {
        return examIssueRepository.save(examIssue)
    }

    fun generateExamIssueList(examId: String): List<ExamIssue> {
        val examIssues = mutableListOf<ExamIssue>().toMutableList()
        val usedQuestions = mutableListOf<Question>().toMutableList()
        val exam = examService.getExamById(examId)
        for (section in 1..exam.sectionsNumber) {
            val topic = questionService.getAllQuestionsBySection(section).random().topic
            val questions = questionService.getAllQuestionsByTopic(topic).sortedBy { it.order }

            questions.forEach { question ->
                examIssues.add(
                    create(
                        ExamIssue(
                            examId, question, examIssues.size
                        )
                    )
                )
            }
            usedQuestions.addAll(questions)
        }
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