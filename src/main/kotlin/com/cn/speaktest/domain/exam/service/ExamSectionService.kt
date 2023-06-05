package com.cn.speaktest.domain.exam.service

import com.cn.speaktest.actor.exam.payload.dto.ExamSectionDto
import com.cn.speaktest.application.advice.NotFoundException
import com.cn.speaktest.domain.exam.model.ExamIssue
import com.cn.speaktest.domain.exam.model.ExamSection
import com.cn.speaktest.domain.exam.model.Suggestion
import com.cn.speaktest.domain.exam.repository.ExamSectionRepository
import org.springframework.stereotype.Service

@Service
class ExamSectionService(private val examSectionRepository: ExamSectionRepository) {
    fun getAllExamSections(): List<ExamSection> {
        return examSectionRepository.findAll()
    }

    fun getExamSectionById(id: String): ExamSection {
        return examSectionRepository.findById(id).orElseThrow { NotFoundException("Exam with id $id not found") }
    }

    fun createExamSection(examSectionDto: ExamSectionDto): ExamSection {
        val examSection = ExamSection(examSectionDto)
        return examSectionRepository.save(examSection)
    }

    fun updateExamSection(id: String, examSectionDto: ExamSectionDto): ExamSection? {
        val existingExamSection = getExamSectionById(id)
        existingExamSection.examSessionId = examSectionDto.examSessionId!!
        existingExamSection.name = examSectionDto.name!!
        existingExamSection.examIssues = examSectionDto.examIssues?.map { ExamIssue(it) }
        existingExamSection.suggestion = examSectionDto.suggestion?.let { Suggestion(it) }
        return examSectionRepository.save(existingExamSection)
    }

    fun deleteExamSection(id: String) {
        examSectionRepository.deleteById(id)
    }
}