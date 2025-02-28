package com.cn.langujet.domain.correction.service.corrector.auto

import com.cn.langujet.domain.answer.AnswerRepository
import com.cn.langujet.domain.correction.service.CorrectAnswerService
import com.cn.langujet.domain.exam.model.ExamSessionEntity
import com.cn.langujet.domain.exam.model.enums.SectionType
import com.cn.langujet.domain.exam.service.ExamService
import com.cn.langujet.domain.exam.service.SectionService
import com.cn.langujet.domain.result.model.SectionResultEntity
import com.cn.langujet.domain.result.service.SectionResultService
import com.rollbar.notifier.Rollbar
import org.springframework.stereotype.Service

@Service
class AutoCorrectorService(
    private val answerRepository: AnswerRepository,
    private val examService: ExamService,
    private val sectionResultService: SectionResultService,
    private val rollbar: Rollbar,
    private val correctAnswerService: CorrectAnswerService,
    private val sectionService: SectionService,
) {
    fun correctExamSection(
        examSession: ExamSessionEntity, resultId: String, sectionOrder: Int, sectionType: SectionType
    ): SectionResultEntity? {
        val exam = examService.getExamById(examSession.examId)
        val section = sectionService.getSectionByExamIdAndOrder(examSession.examId, sectionOrder)
        val correctAnswers = correctAnswerService.getSectionCorrectAnswers(examSession.examId, sectionOrder)
        val answers = answerRepository.findAllByExamSessionIdAndSectionOrder(examSession.id ?: "", sectionOrder)
        try {
            val correctIssuesCount = AutoCorrectionUtil.getCorrectionScore(answers, correctAnswers)
            val score = AutoCorrectionUtil.calculateScore(correctIssuesCount, sectionType, exam.type)
            val recommendation = AutoCorrectionUtil.generateSectionResultMd(section, answers, correctAnswers)
            return sectionResultService.addAutoCorrectionSectionResult(
                resultId = resultId,
                examSessionId = examSession.id ?: "",
                sectionOrder = sectionOrder,
                sectionType = sectionType,
                correctIssuesCount = correctIssuesCount,
                score = score,
                recommendation = recommendation
            )
        } catch (ex: Exception) {
            ex.printStackTrace()
            rollbar.critical(ex, "${ex.message}")
        }
        return null
    }
}