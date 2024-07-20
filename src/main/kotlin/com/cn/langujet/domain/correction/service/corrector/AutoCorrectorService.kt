package com.cn.langujet.domain.correction.service.corrector

import com.cn.langujet.application.advice.LogicalException
import com.cn.langujet.application.advice.NotFoundException
import com.cn.langujet.domain.answer.AnswerRepository
import com.cn.langujet.domain.answer.model.AnswerEntity
import com.cn.langujet.domain.correction.model.CorrectAnswerEntity
import com.cn.langujet.domain.correction.repository.CorrectAnswerRepository
import com.cn.langujet.domain.exam.model.ExamSessionEntity
import com.cn.langujet.domain.exam.model.ExamType
import com.cn.langujet.domain.exam.model.SectionType
import com.cn.langujet.domain.exam.service.ExamService
import com.cn.langujet.domain.result.model.SectionResultEntity
import com.cn.langujet.domain.result.service.SectionResultService
import com.rollbar.notifier.Rollbar
import org.springframework.stereotype.Service

@Service
class AutoCorrectorService(
    private val correctAnswerRepository: CorrectAnswerRepository,
    private val answerRepository: AnswerRepository,
    private val examService: ExamService,
    private val sectionResultService: SectionResultService,
    private val rollbar: Rollbar,
) {
    fun correctExamSection(
        examSession: ExamSessionEntity, resultId: String, sectionOrder: Int, sectionType: SectionType
    ): SectionResultEntity {
        val exam = examService.getExamById(examSession.examId)
        val correctAnswers = correctAnswerRepository.findAllByExamIdAndSectionOrder(examSession.examId, sectionOrder)
        val answers = answerRepository.findAllByExamSessionIdAndSectionOrder(examSession.id ?: "", sectionOrder)
        val correctIssuesCount = calculateCorrectIssuesCount(answers, correctAnswers)
        return sectionResultService.addAutoCorrectionSectionResult(
            resultId = resultId,
            examSessionId = examSession.id ?: "",
            sectionOrder = sectionOrder,
            sectionType = sectionType,
            correctIssuesCount = correctIssuesCount,
            score = calculateScore(correctIssuesCount, sectionType, exam.type),
            recommendation = null
        )
    }
    
    private fun calculateCorrectIssuesCount(answers: List<AnswerEntity>, correctAnswers: List<CorrectAnswerEntity>): Int {
        var correctIssuesCount = 0
        answers.forEach { answer ->
            try {
                val correctAnswer = correctAnswers.find {
                    it.partOrder == answer.partOrder && it.questionOrder == answer.questionOrder
                } ?: throw NotFoundException("Correct Answer not found for Answer with id: ${answer.id}")
                
                correctIssuesCount += when (answer) {
                    is AnswerEntity.TextAnswerEntity -> {
                        if (correctAnswer !is CorrectAnswerEntity.CorrectTextAnswerEntity) throw LogicalException("Correct Answer whit id: ${correctAnswer.id} is not compatible with Answer with id: ${answer.id}")
                        calculateCorrectIssuesCountForTextAnswer(answer, correctAnswer)
                    }
                    
                    is AnswerEntity.TextIssuesAnswerEntity -> {
                        if (correctAnswer !is CorrectAnswerEntity.CorrectTextIssuesAnswerEntity) throw LogicalException("Correct Answer whit id: ${correctAnswer.id} is not compatible with Answer with id: ${answer.id}")
                        calculateCorrectIssuesCountForTextIssuesAnswer(answer, correctAnswer)
                    }
                    
                    is AnswerEntity.TrueFalseAnswerEntity -> {
                        if (correctAnswer !is CorrectAnswerEntity.CorrectTrueFalseAnswerEntity) throw LogicalException("Correct Answer whit id: ${correctAnswer.id} is not compatible with Answer with id: ${answer.id}")
                        calculateCorrectIssuesCountForTrueFalseAnswer(answer, correctAnswer)
                    }
                    
                    is AnswerEntity.MultipleChoiceAnswerEntity -> {
                        if (correctAnswer !is CorrectAnswerEntity.CorrectMultipleChoiceAnswerEntity) throw LogicalException("Correct Answer whit id: ${correctAnswer.id} is not compatible with Answer with id: ${answer.id}")
                        calculateCorrectIssuesCountForMultipleChoiceAnswer(answer, correctAnswer)
                    }
                    
                    else -> throw IllegalArgumentException("Unsupported answer type")
                }
            } catch (ex: Exception) {
                rollbar.critical(ex, "${ex.message}\nCorrection failed for answer: $answer")
            }
        }
        return correctIssuesCount
    }
    
    private fun calculateCorrectIssuesCountForTextAnswer(
        answer: AnswerEntity.TextAnswerEntity, correctAnswer: CorrectAnswerEntity.CorrectTextAnswerEntity
    ): Int {
        return if (correctAnswer.text.pureEqual(answer.text)) 1 else 0
    }
    
    private fun calculateCorrectIssuesCountForTextIssuesAnswer(
        answer: AnswerEntity.TextIssuesAnswerEntity, correctAnswer: CorrectAnswerEntity.CorrectTextIssuesAnswerEntity
    ): Int {
        var correctIssuesCount = 0
        correctAnswer.issues.forEachIndexed { index, correctIssue ->
            answer.issues.getOrNull(index)?.let { answerIssue ->
                correctIssue.firstOrNull {
                    it.pureEqual(answerIssue)
                }?.run {
                    correctIssuesCount++
                }
            }
        }
        return correctIssuesCount
    }
    
    private fun calculateCorrectIssuesCountForTrueFalseAnswer(
        answer: AnswerEntity.TrueFalseAnswerEntity, correctAnswer: CorrectAnswerEntity.CorrectTrueFalseAnswerEntity
    ): Int {
        var correctIssuesCount = 0
        correctAnswer.issues.forEachIndexed { index, issue ->
            if (issue == answer.issues.getOrNull(index)) {
                correctIssuesCount++
            }
        }
        return correctIssuesCount
    }
    
    private fun calculateCorrectIssuesCountForMultipleChoiceAnswer(
        answer: AnswerEntity.MultipleChoiceAnswerEntity, correctAnswer: CorrectAnswerEntity.CorrectMultipleChoiceAnswerEntity
    ): Int {
        var correctIssuesCount = 0
        correctAnswer.issues.forEach { correctIssue ->
            answer.issues.find {
                it.order == correctIssue.order
            }?.let { answerIssue ->
                correctIssue.options.forEach { option ->
                    if (answerIssue.options.contains(option)) {
                        correctIssuesCount++
                    }
                }
            }
        }
        return correctIssuesCount
    }
    
    private fun String.pureEqual(str: String): Boolean {
        val cleanStr1 = this.replace(Regex("[^a-zA-Z0-9 ]"), "").replace("\\s+".toRegex(), " ").lowercase()
        val cleanStr2 = str.replace(Regex("[^a-zA-Z0-9 ]"), "").replace("\\s+".toRegex(), " ").lowercase()
        return cleanStr1 == cleanStr2
    }
    
    private fun calculateScore(
        correctIssuesCount: Int, sectionType: SectionType, examType: ExamType
    ): Double {
        return when (examType) {
            ExamType.IELTS_ACADEMIC -> ScoreCalculator.calculateAcademicIELTSScore(correctIssuesCount, sectionType)
            ExamType.IELTS_GENERAL -> TODO()
        }
    }
    
}