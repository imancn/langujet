package com.cn.speaktest.domain.exam.service

import com.cn.speaktest.actor.exam.payload.request.ExamRequest
import com.cn.speaktest.application.advice.NotFoundException
import com.cn.speaktest.domain.exam.model.ExamInfo
import com.cn.speaktest.domain.exam.model.Price
import com.cn.speaktest.domain.exam.repository.ExamRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service

@Service
class ExamService(
    val examRepository: ExamRepository,
) {

    fun createExam(examInfo: ExamInfo): ExamInfo {
        return examRepository.save(
            examInfo.also { it.id = null }
        )
    }

    fun updateExam(id: String, examRequest: ExamRequest): ExamInfo {
        val existingExam = examRepository.findById(id).orElseThrow { NotFoundException("Exam with id $id not found") }
        examRequest.name?.let { existingExam.name = it }
        examRequest.description?.let { existingExam.description = it }
        examRequest.sectionsNumber?.let { existingExam.sectionsNumber = it }
        examRequest.questionNumber?.let { existingExam.questionNumber = it }
        examRequest.examDuration?.let { existingExam.examDuration = it }
        examRequest.difficulty.let { existingExam.difficulty = it }
        examRequest.priceValue?.let { existingExam.price = Price(examRequest.priceValue, existingExam.price.currency) }
        examRequest.priceCurrency?.let {
            existingExam.price = Price(existingExam.price.value, examRequest.priceCurrency)
        }
        return examRepository.save(existingExam)
    }

    fun getExamById(id: String?): ExamInfo {
        return examRepository.findById(id!!).orElseThrow { NotFoundException("Exam with id $id not found") }
    }

    fun getAllExams(): List<ExamInfo> {
        return examRepository.findAll()
    }

    fun getAllExamsByFilters(
        id: String?,
        name: String?,
        sectionsNumber: Int?,
        questionNumber: Int?,
        examDuration: Long?,
        pageRequest: PageRequest
    ): Page<ExamInfo> {
        return examRepository.findAllExamsByFilters(
            id,
            name,
            sectionsNumber,
            questionNumber,
            examDuration,
            pageRequest,
        )
    }
}
