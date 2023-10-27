package com.cn.langujet.domain.exam.service

import com.cn.langujet.actor.exam.payload.ExamDto
import com.cn.langujet.application.advice.NotFoundException
import com.cn.langujet.domain.exam.model.Exam
import com.cn.langujet.domain.exam.model.ExamType
import com.cn.langujet.domain.exam.repository.ExamRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service

@Service
class ExamService(
    val examRepository: ExamRepository,
) {

    fun createExam(exam: Exam): Exam {
        return examRepository.save(exam.also { it.id = null })
    }

    fun updateExam(id: String, exam: ExamDto): Exam {
        val existingExam = getExamById(id)
        exam.name?.let { existingExam.name = it }
        exam.examType.let { existingExam.examType = it }
        exam.description?.let { existingExam.description = it }
        exam.sectionsNumber?.let { existingExam.sectionsNumber = it }
        exam.questionNumber?.let { existingExam.questionNumber = it }
        exam.examDuration?.let { existingExam.examDuration = it }
        return examRepository.save(existingExam)
    }

    fun getExamById(id: String): Exam {
        return examRepository.findById(id).orElseThrow { NotFoundException("Exam with id $id not found") }
    }

    fun getAllExams(): List<Exam> {
        return examRepository.findAll()
    }

    fun getAllExamsByFilters(
        id: String?,
        name: String?,
        sectionsNumber: Int?,
        questionNumber: Int?,
        examDuration: Long?,
        pageRequest: PageRequest
    ): Page<Exam> {
        return examRepository.findAllExamsByFilters(
            id,
            name,
            sectionsNumber,
            questionNumber,
            examDuration,
            pageRequest,
        )
    }

    fun getRandomExamIdByType(examType: ExamType): String {
        return examRepository.findIdsByExamType(examType).random()
    }
}
