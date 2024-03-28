package com.cn.langujet.domain.exam.service

import com.cn.langujet.actor.exam.payload.ExamDTO
import com.cn.langujet.actor.util.models.CustomPage
import com.cn.langujet.actor.util.models.toCustomPage
import com.cn.langujet.application.advice.InvalidInputException
import com.cn.langujet.application.advice.NotFoundException
import com.cn.langujet.domain.exam.model.Exam
import com.cn.langujet.domain.exam.model.ExamType
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
        exam.name?.let { existingExam.name = it }
        exam.examType?.let { existingExam.type = it }
        exam.description?.let { existingExam.description = it }
        exam.sectionsNumber?.let { existingExam.sectionsNumber = it }
        exam.questionNumber?.let { existingExam.questionNumber = it }
        exam.examDuration?.let { existingExam.examDuration = it }
        exam.active?.let { existingExam.active = it }
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
    
    fun getRandomActiveExamIdByType(examType: ExamType): String {
        return examRepository.findAllByTypeAndActive(examType, true).mapNotNull { it.id }.random()
    }
}
