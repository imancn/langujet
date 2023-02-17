package com.cn.speaktest.controller

import com.cn.speaktest.advice.InvalidInputException
import com.cn.speaktest.advice.Message
import com.cn.speaktest.advice.NotFoundException
import com.cn.speaktest.advice.toOkMessage
import com.cn.speaktest.exam.api.ExamService
import com.cn.speaktest.exam.model.Exam
import com.cn.speaktest.exam.model.Question
import com.cn.speaktest.student.payload.request.AddQuestionRequest
import com.cn.speaktest.exam.repository.ExamRepository
import com.cn.speaktest.exam.repository.ExamRequestRepository
import com.cn.speaktest.exam.repository.QuestionRepository
import com.cn.speaktest.professor.ProfessorRepository
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@CrossOrigin(origins = ["*"], maxAge = 3600)
@RestController
@RequestMapping("/api/admin")
@Validated
class AdminController(
    val examRequestRepository: ExamRequestRepository,
    val professorRepository: ProfessorRepository,
    val questionRepository: QuestionRepository,
    val examRepository: ExamRepository,
    val examService: ExamService
) {
    @GetMapping("/get-professors")
    @PreAuthorize("hasRole('ADMIN')")
    fun getProfessors(@RequestHeader("Authorization") auth: String?): Message {
        return professorRepository.findAll().sortedBy {
            it.fullName
        }.toOkMessage()
    }

    @GetMapping("/get-exam-requests")
    @PreAuthorize("hasRole('ADMIN')")
    fun getExamRequests(@RequestHeader("Authorization") auth: String?): Message {
        return examRequestRepository.findAll().sortedByDescending {
            it.date
        }.toOkMessage()
    }

    @PostMapping("/confirm-exam")
    @PreAuthorize("hasRole('ADMIN')")
    fun confirmExam(
        @RequestHeader("Authorization") auth: String?,
        @NotBlank @RequestParam examRequestId: String,
        @NotBlank @RequestParam professorId: String,
    ): Message {
        val examRequest = examRequestRepository.findById(examRequestId).orElseThrow {
            NotFoundException("ExamRequest not found")
        }
        val professor = professorRepository.findById(professorId).orElseThrow {
            NotFoundException("Professor not found")
        }

        val exam = examRepository.save(
            Exam(
                examRequest.student,
                professor,
                examRequest.date
            )
        )

        val examIssues = examService.generateExamIssueList(
            exam.id ?: throw RuntimeException("exam.id must not be null")
        )

        examRepository.save(exam.also { it.examIssues = examIssues })
        examRequestRepository.delete(examRequest)

        return Message(null, "Exam have been confirmed")
    }

    @PostMapping("/add-question")
    @PreAuthorize("hasRole('ADMIN')")
    fun addQuestion(
        @RequestHeader("Authorization") auth: String?,
        @Valid @RequestBody addQuestionRequest: AddQuestionRequest
    ): Message {
        val section = addQuestionRequest.section
        val topic = addQuestionRequest.topic
        val order = addQuestionRequest.order

        val isDuplicate = questionRepository.existsBySectionAndTopicAndOrder(section, topic, order)
        if (isDuplicate)
            throw InvalidInputException("A question with section: $section, topic: $topic, order: $order does exist")

        val question = questionRepository.save(
            Question(
                section,
                topic,
                order,
                addQuestionRequest.text,
            )
        )
        return Message(question, "Question added successfully")
    }
}