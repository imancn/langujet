package com.cn.langujet.domain.exam.service

import com.cn.langujet.actor.exam.payload.dto.ExamSectionDto
import com.cn.langujet.application.advice.NotFoundException
import com.cn.langujet.domain.exam.model.*
import com.cn.langujet.domain.exam.repository.ExamSectionRepository
import org.springframework.stereotype.Service

@Service
class ExamSectionService(
    private val examSectionRepository: ExamSectionRepository,
    private val examSessionService: ExamSessionService,
) {
    fun getAllExamSections(): List<ExamSection> {
        return examSectionRepository.findAll()
    }

    fun getExamSectionById(id: String): ExamSection {
        return examSectionRepository.findById(id).orElseThrow { NotFoundException("Exam with id $id not found") }
    }

    fun createExamSection(examSectionDto: ExamSectionDto): ExamSectionDto {
        val examSession = examSessionService.getExamSessionById(examSectionDto.examSessionId)
        val exam = examSession.exam
        val section = examSession.examSections?.find {
            it.id == examSectionDto.sectionId
        }?.section ?: throw NotFoundException("Section with id ${examSectionDto.examSessionId} not found")
        val examSection = ExamSection(examSectionDto, exam, section)
        return ExamSectionDto(examSectionRepository.save(examSection))
    }

    fun updateExamSection(id: String, examSectionDto: ExamSectionDto): ExamSection? {
        val existingExamSection = getExamSectionById(id)
        examSectionDto.examSessionId?.let { existingExamSection.examSessionId = it }
        examSectionDto.examIssues?.let { list -> existingExamSection.examIssues = list.map { ExamIssue(it) } }
        examSectionDto.suggestion?.let { existingExamSection.suggestion = Suggestion(it) }
        return examSectionRepository.save(existingExamSection)
    }

    fun deleteExamSection(id: String) {
        examSectionRepository.deleteById(id)
    }
}