package com.cn.langujet.domain.exam.service

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
        exam.id = null
        return examRepository.save(exam.also { it.id = null })
    }

    fun updateExam(id: String, exam: Exam): Exam {
        val existingExam = getExamById(id)
        exam.name.let { existingExam.name = it }
        exam.examType.let { existingExam.examType = it }
        exam.description.let { existingExam.description = it }
        exam.sectionsNumber.let { existingExam.sectionsNumber = it }
        exam.questionNumber.let { existingExam.questionNumber = it }
        exam.examDuration.let { existingExam.examDuration = it }
        return examRepository.save(existingExam)
    }

    fun getExamById(id: String): Exam {
        return examRepository.findById(id).orElseThrow { NotFoundException("Exam with id $id not found") }
    }

    fun getAllExams(): List<Exam> {
        return examRepository.findAll()
    }

    fun getAllExamsByName(
        name: String,
        pageRequest: PageRequest
    ): Page<Exam> {
        return examRepository.findAllByNameContainingIgnoreCaseOrderByNameAsc(
            name,
            pageRequest,
        )
    }

    fun getRandomExamIdByType(examType: ExamType): String {
        return examRepository.findIdsByExamType(examType).random()
    }
}
