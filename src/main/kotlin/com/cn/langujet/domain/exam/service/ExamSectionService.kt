package com.cn.langujet.domain.exam.service

import com.cn.langujet.actor.exam.payload.dto.ExamSectionDto
import com.cn.langujet.application.advice.NotFoundException
import com.cn.langujet.domain.exam.model.*
import com.cn.langujet.domain.exam.repository.ExamSectionRepository
import com.cn.langujet.domain.exam.repository.ExamSessionRepository
import org.springframework.stereotype.Service

@Service
class ExamSectionService(
    private val examSectionRepository: ExamSectionRepository,
    private val examSessionRepository: ExamSessionRepository
) {
    fun getAllExamSections(): List<ExamSection> {
        return examSectionRepository.findAll()
    }

    fun getExamSectionById(id: String): ExamSection {
        return examSectionRepository.findById(id).orElseThrow { NotFoundException("Exam with id $id not found") }
    }

    fun createExamSection(examSectionDto: ExamSectionDto): ExamSectionDto {
        val examSession = getExamSessionById(examSectionDto.examSessionId)
        val exam = examSession.exam
        val section = examSession.examSections.find {
            it.id == examSectionDto.sectionId
        }?.section ?: throw NotFoundException("Section with id ${examSectionDto.examSessionId} not found")
        val examSection = ExamSection(examSectionDto, exam, section)
        return ExamSectionDto(examSectionRepository.save(examSection))
    }

    fun updateExamSection(id: String, examSectionDto: ExamSectionDto): ExamSection {
        val existingExamSection = getExamSectionById(id)
        examSectionDto.examSessionId?.let { existingExamSection.examSessionId = it }
        examSectionDto.examIssues?.let { list -> existingExamSection.examIssues = list.map { ExamIssue(it) } }
        examSectionDto.suggestion?.let { existingExamSection.suggestion = Suggestion(it) }
        return examSectionRepository.save(existingExamSection)
    }

    fun deleteExamSection(id: String) {
        examSectionRepository.deleteById(id)
    }

    fun getExamSectionBySuggestionId(suggestionId: String): ExamSection {
        return examSectionRepository.findBySuggestion_Id(suggestionId).orElseThrow {
            throw NotFoundException("ExamSection not found with id: $suggestionId")
        }
    }

    fun getExamSessionById(id: String?): ExamSession {
        return examSessionRepository.findById(id!!).orElseThrow {
            NotFoundException("ExamSession with id: $id not found")
        }
    }
}