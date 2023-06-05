package com.cn.speaktest.domain.exam.service

import com.cn.speaktest.actor.exam.payload.dto.ExamMetaDto
import com.cn.speaktest.application.advice.NotFoundException
import com.cn.speaktest.domain.exam.model.ExamMeta
import com.cn.speaktest.domain.exam.repository.ExamMetaRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service

@Service
class ExamMetaService(
    val examMetaRepository: ExamMetaRepository,
) {

    fun createExam(examMeta: ExamMeta): ExamMeta {
        return examMetaRepository.save(
            examMeta.also { it.id = null }
        )
    }

    fun updateExam(id: String, examInfo: ExamMetaDto): ExamMeta {
        val existingExam = examMetaRepository.findById(id).orElseThrow { NotFoundException("Exam with id $id not found") }
        examInfo.name.let { existingExam.name = it }
        examInfo.description.let { existingExam.description = it }
        examInfo.sectionsNumber.let { existingExam.sectionsNumber = it }
        examInfo.questionNumber.let { existingExam.questionNumber = it }
        examInfo.examDuration.let { existingExam.examDuration = it }
        examInfo.difficulty.let { existingExam.difficulty = it }
        examInfo.price.value.let { existingExam.price.value =  it}
        examInfo.price.currency.let { existingExam.price.currency = it }
        return examMetaRepository.save(existingExam)
    }

    fun getExamById(id: String?): ExamMeta {
        return examMetaRepository.findById(id!!).orElseThrow { NotFoundException("Exam with id $id not found") }
    }

    fun getAllExams(): List<ExamMeta> {
        return examMetaRepository.findAll()
    }

    fun getAllExamsByFilters(
        id: String?,
        name: String?,
        sectionsNumber: Int?,
        questionNumber: Int?,
        examDuration: Long?,
        pageRequest: PageRequest
    ): Page<ExamMeta> {
        return examMetaRepository.findAllExamsByFilters(
            id,
            name,
            sectionsNumber,
            questionNumber,
            examDuration,
            pageRequest,
        )
    }
}
