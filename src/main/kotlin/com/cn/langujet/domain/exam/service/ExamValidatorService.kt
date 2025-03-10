package com.cn.langujet.domain.exam.service

import com.cn.langujet.application.advice.UnprocessableException
import com.cn.langujet.domain.correction.service.CorrectAnswerService
import com.cn.langujet.domain.exam.model.*
import com.cn.langujet.domain.exam.model.enums.ExamType
import com.cn.langujet.domain.exam.model.enums.SectionType
import com.cn.langujet.domain.exam.model.section.SectionEntity
import com.cn.langujet.domain.exam.repository.SectionRepository
import org.springframework.stereotype.Service

@Service
class ExamValidatorService(
    private val sectionRepository: SectionRepository,
    private val correctAnswerService: CorrectAnswerService
) {
    fun validate(exam: ExamEntity) {
        val sections = sectionRepository.findAllByExamId(exam.id ?: "")
        validateSections(exam, sections)
        sections.forEach { section ->
            validateSectionParts(section)
            validateSectionPartQuestions(section)
            validateCorrectAnswers(section, exam.type)
        }
    }
    
    private fun validateSections(
        exam: ExamEntity,
        sections: List<SectionEntity>,
    ) {
        val sectionOrders = sections.map { it.order }
        if (exam.sectionsNumber != sections.size) {
            throw UnprocessableException("Section numbers must be ${exam.sectionsNumber} but found ${sections.size}")
        }
        if (sectionOrders.size != sectionOrders.distinct().size) {
            throw UnprocessableException("Duplicate section orders must be removed")
        }
        if (sectionOrders.maxOf { it } != sectionOrders.size) {
            throw UnprocessableException("Section orders must be iterative and starts by 1")
        }
    }
    
    private fun validateSectionParts(section: SectionEntity) {
        val partsOrders = section.parts.map { it.order }
        if (partsOrders.size != partsOrders.distinct().size) {
            throw UnprocessableException("Duplicate part orders must be removed for sectionOrder: ${section.order}")
        }
        if (partsOrders.maxOf { it } != partsOrders.size) {
            throw UnprocessableException("Part orders must be iterative and starts by 1 for sectionOrder: ${section.order}")
        }
    }
    
    private fun validateSectionPartQuestions(section: SectionEntity) {
        section.parts.forEach { part ->
            val questionOrders = part.getQuestions().map { it.order }
            if (questionOrders.size != questionOrders.distinct().size) {
                throw UnprocessableException("Duplicate question orders must be removed for sectionOrder: ${section.order}, partOrder: ${part.order}")
            }
            if (questionOrders.maxOf { it } != questionOrders.size) {
                throw UnprocessableException("Question orders must be iterative and starts by 1 for sectionOrder: ${section.order}, partOrder: ${part.order}")
            }
        }
    }
    
    private fun validateCorrectAnswers(section: SectionEntity, examType: ExamType) {
        if (arrayOf(ExamType.IELTS_GENERAL, ExamType.IELTS_ACADEMIC).contains(examType) and arrayOf(SectionType.READING, SectionType.LISTENING).contains(section.sectionType)) {
            val correctAnswers = correctAnswerService.getSectionCorrectAnswers(section.examId, section.order)
            section.parts.forEach { part ->
                part.getQuestions().forEach { question ->
                    correctAnswers.find { correctAnswer ->
                        correctAnswer.examId == section.examId &&
                            correctAnswer.sectionOrder == section.order &&
                            correctAnswer.partOrder == part.order &&
                            correctAnswer.questionOrder == question.order
                    } ?: throw UnprocessableException("There is no correct answer for examId: ${section.examId}, sectionOrder: ${section.order}, partOrder: ${part.order}, questionOrder: ${question.order}")
                }
            }
        }
    }
}