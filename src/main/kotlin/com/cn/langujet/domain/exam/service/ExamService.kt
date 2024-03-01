package com.cn.langujet.domain.exam.service

import com.cn.langujet.actor.exam.payload.ExamDTO
import com.cn.langujet.application.advice.NotFoundException
import com.cn.langujet.domain.exam.model.ExamType
import com.cn.langujet.domain.exam.repository.ExamRepository
import org.springframework.data.domain.Page
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

    fun updateExam(id: String, exam: ExamDTO): ExamDTO {
        val existingExam = getExamById(id)
        exam.name?.let { existingExam.name = it }
        exam.examType?.let { existingExam.examType = it }
        exam.description?.let { existingExam.description = it }
        exam.sectionsNumber?.let { existingExam.sectionsNumber = it }
        exam.questionNumber?.let { existingExam.questionNumber = it }
        exam.examDuration?.let { existingExam.examDuration = it }
        exam.active?.let { existingExam.active = it }
        return ExamDTO(
            examRepository.save(existingExam.toExam())
        )
    }

    fun getExamById(id: String): ExamDTO {
        return ExamDTO(
            examRepository.findById(id).orElseThrow { NotFoundException("Exam with id $id not found") }
        )
    }

    fun getAllExams(): List<ExamDTO> {
        return examRepository.findAll().map { ExamDTO(it) }
    }

    fun getAllExamsByName(
        name: String,
        pageRequest: PageRequest
    ): Page<ExamDTO> {
        return examRepository.findAllByNameContainingIgnoreCaseOrderByNameAsc(
            name,
            pageRequest,
        ).map { ExamDTO(it) }
    }

    fun getRandomActiveExamIdByType(examType: ExamType): String {
        return examRepository.findAllByTypeAndActive(examType, true).mapNotNull { it.id }.random()
    }
}
