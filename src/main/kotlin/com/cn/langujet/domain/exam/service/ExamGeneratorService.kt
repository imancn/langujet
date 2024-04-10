package com.cn.langujet.domain.exam.service

import com.cn.langujet.domain.exam.model.Exam
import com.cn.langujet.domain.exam.repository.ExamRepository
import com.cn.langujet.domain.exam.repository.ExamSessionRepository
import org.springframework.stereotype.Service
import kotlin.random.Random

@Service
class ExamGeneratorService(
    private val examRepository: ExamRepository,
    private val examSessionRepository: ExamSessionRepository,
    private val examVariantService: ExamVariantService
) {
    
    fun getRandomStudentAvailableExam(studentUserId: String, examVariantId: String): Exam {
        val examIds = examSessionRepository.findByStudentUserId(studentUserId, examVariantId).map { it.examId }
        val examVariant = examVariantService.getExamVariantById(examVariantId)
        val exams = examRepository.findAllByTypeAndActiveAndSectionsNumberAndIdNotIn(
            examVariant.examType,
            true,
            examVariant.sectionTypes.count(),
            examIds
        )
        return if (exams.isNotEmpty()) {
            exams.random(Random(System.currentTimeMillis()))
        } else {
            examRepository.findAllByTypeAndActive(examVariant.examType, true).random(Random(System.currentTimeMillis()))
        }
    }
}