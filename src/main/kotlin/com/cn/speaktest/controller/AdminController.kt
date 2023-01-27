package com.cn.speaktest.controller

import com.cn.speaktest.advice.InvalidInputException
import com.cn.speaktest.advice.Message
import com.cn.speaktest.advice.toOkMessage
import com.cn.speaktest.model.Exam
import com.cn.speaktest.model.Question
import com.cn.speaktest.payload.request.AddQuestionRequest
import com.cn.speaktest.repository.exam.ExamRepository
import com.cn.speaktest.repository.exam.ExamRequestRepository
import com.cn.speaktest.repository.exam.QuestionRepository
import com.cn.speaktest.repository.user.ProfessorRepository
import com.cn.speaktest.service.ExamService
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@CrossOrigin(origins = ["*"], maxAge = 3600)
@RestController
@RequestMapping("/api/admin")
class AdminController(
    val examRequestRepository: ExamRequestRepository,
    val professorRepository: ProfessorRepository,
    val questionRepository: QuestionRepository,
    val examRepository: ExamRepository,
    val examService: ExamService
) {
    @GetMapping("/get-professors")
    @PreAuthorize("hasRole('ADMIN')")
    fun getProfessors(@RequestHeader("Authorization") auth: String): Message {
        return professorRepository.findAll().sortedBy {
            it.fullName
        }.toOkMessage()
    }

    @GetMapping("/get-exam-requests")
    @PreAuthorize("hasRole('ADMIN')")
    fun getExamRequests(@RequestHeader("Authorization") auth: String): Message {
        return examRequestRepository.findAll().sortedByDescending {
            it.date
        }.toOkMessage()
    }

    @PostMapping("/confirm-exam")
    @PreAuthorize("hasRole('ADMIN')")
    fun confirmExam(
        @RequestHeader("Authorization") auth: String,
        @RequestParam examRequestId: String,
        @RequestParam professorId: String,
    ): Message {
        val examRequest = examRequestRepository.findById(examRequestId).get()
        val professor = professorRepository.findById(professorId).get()

        val exam = examRepository.save(
            Exam(
                examRequest.student,
                professor,
                examRequest.date
            )
        )

        val examIssues = examService.generateExamIssueList(
            exam.id ?: throw InvalidInputException("exam.id must not be null")
        )

        examRepository.save(exam.also { it.examIssues = examIssues })
        examRequestRepository.delete(examRequest)

        return Message(null, "Exam have been confirmed")
    }

    @PostMapping("/add-question")
    @PreAuthorize("hasRole('ADMIN')")
    fun editProfile(
        @RequestHeader("Authorization") auth: String,
        @RequestBody addQuestionRequest: AddQuestionRequest
    ): Message {
        questionRepository.save(
            Question(
                addQuestionRequest.section,
                addQuestionRequest.topic,
                addQuestionRequest.order,
                addQuestionRequest.text,
            )
        )

        return Message(null, "Question added successfully")
    }
}