package com.cn.langujet.domain.correction.service.corrector.auto

import com.cn.langujet.application.arch.models.entity.Entity
import com.cn.langujet.domain.answer.AnswerRepository
import com.cn.langujet.domain.correction.service.CorrectAnswerService
import com.cn.langujet.domain.exam.model.ExamSessionEntity
import com.cn.langujet.domain.exam.model.enums.SectionType
import com.cn.langujet.domain.exam.service.ExamService
import com.cn.langujet.domain.exam.service.PartService
import com.cn.langujet.domain.exam.service.QuestionService
import com.cn.langujet.domain.exam.service.SectionService
import com.cn.langujet.domain.result.model.SectionResultEntity
import com.cn.langujet.domain.result.service.SectionResultService
import com.rollbar.notifier.Rollbar
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.stereotype.Service

@Service
class AutoCorrectorService(
    private val answerRepository: AnswerRepository,
    private val examService: ExamService,
    private val sectionResultService: SectionResultService,
    private val rollbar: Rollbar,
    private val correctAnswerService: CorrectAnswerService,
    private val sectionService: SectionService,
    private val partService: PartService,
    private val questionService: QuestionService,
) {
    fun correctExamSection(
        examSession: ExamSessionEntity, resultId: Long, sectionOrder: Int, sectionType: SectionType
    ): SectionResultEntity? {
        val exam = examService.getById(examSession.examId)
        val section = sectionService.getSectionByExamIdAndOrder(examSession.examId, sectionOrder)
        val correctAnswers = correctAnswerService.getSectionCorrectAnswers(examSession.examId, sectionOrder)
        val answers =
            answerRepository.findAllByExamSessionIdAndSectionOrder(examSession.id ?: Entity.UNKNOWN_ID, sectionOrder)
        try {
            val correctIssuesCount = AutoCorrectionUtil.getCorrectionScore(answers, correctAnswers)
            val score = AutoCorrectionUtil.calculateScore(correctIssuesCount, sectionType, exam.type)
            val parts = partService.find(Criteria.where("sectionId").`is`(section.id))
            val questions = questionService.find(Criteria.where("sectionId").`is`(section.id))
            val recommendation =
                AutoCorrectionUtil.generateSectionResultMd(section, parts, questions, answers, correctAnswers)
            return sectionResultService.addAutoCorrectionSectionResult(
                resultId = resultId,
                examSessionId = examSession.id ?: Entity.UNKNOWN_ID,
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