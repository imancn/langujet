package com.cn.langujet.domain.exam.service

import com.cn.langujet.actor.exam.payload.ExamDTO
import com.cn.langujet.application.arch.controller.payload.response.PageResponse
import com.cn.langujet.application.arch.controller.payload.response.toCustomPage
import com.cn.langujet.application.arch.advice.InvalidInputException
import com.cn.langujet.application.arch.advice.UnprocessableException
import com.cn.langujet.domain.exam.model.ExamEntity
import com.cn.langujet.domain.exam.repository.ExamRepository
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service

@Service
class ExamService(
    private val examRepository: ExamRepository,
    private val examValidatorService: ExamValidatorService,
) {
    
    fun createExam(exam: ExamDTO): ExamDTO {
        exam.id = null
        exam.active = false
        return ExamDTO(
            examRepository.save(exam.toExam())
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
        if (exam.active) {
            examValidatorService.validate(existingExam)
        }
        return ExamDTO(
            examRepository.save(existingExam)
        )
    }
    
    fun getExamById(id: String): ExamEntity {
        return examRepository.findById(id).orElseThrow { UnprocessableException("Exam with id $id not found") }
    }
    
    fun getAllExams(): List<ExamDTO> {
        return examRepository.findAll().map { ExamDTO(it) }
    }
    
    fun getAllExamsByName(
        name: String, pageRequest: PageRequest
    ): PageResponse<ExamDTO> {
        return examRepository.findAllByNameContainingIgnoreCaseOrderByNameAsc(
            name, pageRequest
        ).map { ExamDTO(it) }.toCustomPage()
    }
}
