package com.cn.speaktest.admin

import com.cn.speaktest.admin.payload.request.AddQuestionRequest
import com.cn.speaktest.advice.InvalidInputException
import com.cn.speaktest.advice.NotFoundException
import com.cn.speaktest.exam.api.ExamService
import com.cn.speaktest.exam.model.Question
import com.cn.speaktest.exam.repository.ExamRequestRepository
import com.cn.speaktest.exam.repository.QuestionRepository
import com.cn.speaktest.professor.ProfessorRepository
import org.springframework.stereotype.Service

@Service
class AdminService(
    val examRequestRepository: ExamRequestRepository,
    val professorRepository: ProfessorRepository,
    val examService: ExamService,
    val questionRepository: QuestionRepository,
) {

    fun addQuestion(addQuestionRequest: AddQuestionRequest?): Question {
        val section = addQuestionRequest!!.section!!
        val topic = addQuestionRequest.topic!!
        val order = addQuestionRequest.order!!

        if (questionRepository.existsBySectionAndTopicAndOrder(section, topic, order))
            throw InvalidInputException("A question with section: $section, topic: $topic, order: $order does exist")

        return questionRepository.save(
            Question(
                section,
                topic,
                order,
                addQuestionRequest.text!!,
            )
        )
    }

    fun confirmExam(examRequestId: String?, professorId: String?): String? {
        val examRequest = examRequestRepository.findById(examRequestId!!).orElseThrow {
            NotFoundException("ExamRequest not found")
        }
        val professor = professorRepository.findById(professorId!!).orElseThrow {
            NotFoundException("Professor not found")
        }

        return examService.submitExam(examRequest, professor).id
    }

    fun getProfessors() = professorRepository.findAll().sortedBy {
        it.fullName
    }

    fun getExamRequests() = examRequestRepository.findAll().sortedByDescending {
        it.date
    }
}