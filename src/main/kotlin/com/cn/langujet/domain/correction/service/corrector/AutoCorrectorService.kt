package com.cn.langujet.domain.correction.service.corrector

import com.cn.langujet.application.advice.LogicalException
import com.cn.langujet.application.advice.NotFoundException
import com.cn.langujet.domain.answer.AnswerRepository
import com.cn.langujet.domain.answer.model.Answer
import com.cn.langujet.domain.correction.model.CorrectAnswer
import com.cn.langujet.domain.correction.model.CorrectionEntity
import com.cn.langujet.domain.correction.repository.CorrectAnswerRepository
import com.cn.langujet.domain.exam.model.ExamSession
import com.cn.langujet.domain.exam.model.ExamType
import com.cn.langujet.domain.exam.model.SectionType
import com.cn.langujet.domain.exam.service.ExamService
import com.cn.langujet.domain.result.service.ResultService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Service

@Service
class AutoCorrectorService(
    private val correctAnswerRepository: CorrectAnswerRepository,
    private val answerRepository: AnswerRepository,
    private val examService: ExamService,
) {
    @Autowired @Lazy private lateinit var resultService: ResultService
    
    fun correctExamSection(examSession: ExamSession, correction: CorrectionEntity) {
        val exam = examService.getExamById(examSession.examId)
        val correctAnswers = correctAnswerRepository.findAllByExamIdAndSectionOrder(examSession.examId, correction.sectionOrder)
        val answers = answerRepository.findAllByExamSessionIdAndSectionOrder(examSession.id ?: "", correction.sectionOrder)
        val correctIssuesCount = calculateCorrectIssuesCount(answers, correctAnswers)
        resultService.addSectionResult(
            correction = correction,
            correctIssuesCount = correctIssuesCount,
            score = calculateScore(correctIssuesCount, correction.sectionType, exam.type),
        )
    }
    
    private fun calculateCorrectIssuesCount(answers: List<Answer>, correctAnswers: List<CorrectAnswer>): Int {
        var correctIssuesCount = 0
        answers.forEach { answer ->
            val correctAnswer = correctAnswers.find {
                it.partOrder == answer.partOrder && it.questionOrder == answer.questionOrder
            } ?: throw NotFoundException("Correct Answer not found for Answer with id: ${answer.id}")
            
            correctIssuesCount += when (answer) {
                is Answer.TextAnswer -> {
                    if (correctAnswer !is CorrectAnswer.CorrectTextAnswer) throw LogicalException("Correct Answer whit id: ${correctAnswer.id} is not compatible with Answer with id: ${answer.id}")
                    calculateCorrectIssuesCountForTextAnswer(answer, correctAnswer)
                }
                
                is Answer.TextIssuesAnswer -> {
                    if (correctAnswer !is CorrectAnswer.CorrectTextIssuesAnswer) throw LogicalException("Correct Answer whit id: ${correctAnswer.id} is not compatible with Answer with id: ${answer.id}")
                    calculateCorrectIssuesCountForTextIssuesAnswer(answer, correctAnswer)
                }
                
                is Answer.TrueFalseAnswer -> {
                    if (correctAnswer !is CorrectAnswer.CorrectTrueFalseAnswer) throw LogicalException("Correct Answer whit id: ${correctAnswer.id} is not compatible with Answer with id: ${answer.id}")
                    calculateCorrectIssuesCountForTrueFalseAnswer(answer, correctAnswer)
                }
                
                is Answer.MultipleChoiceAnswer -> {
                    if (correctAnswer !is CorrectAnswer.CorrectMultipleChoiceAnswer) throw LogicalException("Correct Answer whit id: ${correctAnswer.id} is not compatible with Answer with id: ${answer.id}")
                    calculateCorrectIssuesCountForMultipleChoiceAnswer(answer, correctAnswer)
                }
                
                else -> throw IllegalArgumentException("Unsupported answer type")
            }
        }
        return correctIssuesCount
    }
    
    private fun calculateCorrectIssuesCountForTextAnswer(
        answer: Answer.TextAnswer, correctAnswer: CorrectAnswer.CorrectTextAnswer
    ): Int {
        return if (correctAnswer.text.pureEqual(answer.text)) 1 else 0
    }
    
    private fun calculateCorrectIssuesCountForTextIssuesAnswer(
        answer: Answer.TextIssuesAnswer, correctAnswer: CorrectAnswer.CorrectTextIssuesAnswer
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
        answer: Answer.TrueFalseAnswer, correctAnswer: CorrectAnswer.CorrectTrueFalseAnswer
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
        answer: Answer.MultipleChoiceAnswer, correctAnswer: CorrectAnswer.CorrectMultipleChoiceAnswer
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