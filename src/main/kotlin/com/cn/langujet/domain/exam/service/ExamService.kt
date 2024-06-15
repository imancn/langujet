package com.cn.langujet.domain.exam.service

import com.cn.langujet.actor.exam.payload.ExamDTO
import com.cn.langujet.actor.util.models.CustomPage
import com.cn.langujet.actor.util.models.toCustomPage
import com.cn.langujet.application.advice.InvalidInputException
import com.cn.langujet.application.advice.NotFoundException
import com.cn.langujet.domain.exam.model.Exam
import com.cn.langujet.domain.exam.repository.ExamRepository
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service

@Service
class ExamService(
    val examRepository: ExamRepository,
) {
    
    fun createExam(exam: ExamDTO): ExamDTO {
        exam.id = null
        return ExamDTO(
            examRepository.save(exam.also { it.id = null }.toExam())
        )
    }
    
    fun updateExam(exam: ExamDTO): ExamDTO {
        if (exam.id.isNullOrBlank()) throw InvalidInputException("Exam Id is empty")
        val existingExam = getExamById(exam.id ?: "")
        existingExam.name = exam.name
        existingExam.type = exam.examType
        existingExam.description = exam.description
        existingExam.sectionsNumber = exam.sectionsNumber
        existingExam.questionNumber = exam.questionNumber
        existingExam.examDuration = exam.examDuration
        existingExam.active = exam.active
        return ExamDTO(
            examRepository.save(existingExam)
        )
    }
    
    fun getExamById(id: String): Exam {
        return examRepository.findById(id).orElseThrow { NotFoundException("Exam with id $id not found") }
    }
    
    fun getAllExams(): List<ExamDTO> {
        return examRepository.findAll().map { ExamDTO(it) }
    }
    
    fun getAllExamsByName(
        name: String, pageRequest: PageRequest
    ): CustomPage<ExamDTO> {
        return examRepository.findAllByNameContainingIgnoreCaseOrderByNameAsc(
            name, pageRequest
        ).map { ExamDTO(it) }.toCustomPage()
    }
}
