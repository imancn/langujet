package com.cn.langujet.domain.correction.service.corrector.auto

import com.cn.langujet.application.arch.advice.InternalServerError
import com.cn.langujet.domain.answer.model.AnswerEntity
import com.cn.langujet.domain.correction.model.CorrectAnswerEntity
import com.cn.langujet.domain.exam.model.enums.ExamType
import com.cn.langujet.domain.exam.model.enums.SectionType
import com.cn.langujet.domain.exam.model.section.SectionEntity
import com.cn.langujet.domain.exam.model.section.part.PartEntity
import com.cn.langujet.domain.exam.model.section.part.questions.QuestionEntity

class AutoCorrectionUtil {
    companion object {
        fun getCorrectionScore(answers: List<AnswerEntity>, correctAnswers: List<CorrectAnswerEntity>): Int {
            var correctIssuesCount = 0
            correctAnswers.sortedBy { "${it.partId}-${it.questionId}" }.forEach { correctAnswer ->
                val answer = answers.find {
                    it.partOrder == correctAnswer.partId && it.questionOrder == correctAnswer.questionId
                }
                if (answer != null) {
                    correctIssuesCount += when (correctAnswer) {
                        
                        is CorrectAnswerEntity.CorrectTextAnswerEntity -> {
                            if (answer !is AnswerEntity.TextAnswerEntity) throw InternalServerError("Answer whit id: ${correctAnswer.id} is not compatible with Answer with id: ${correctAnswer.id}")
                            calculateCorrectIssuesCountForTextAnswer(answer, correctAnswer)
                        }
                        
                        is CorrectAnswerEntity.CorrectTextIssuesAnswerEntity -> {
                            if (answer !is AnswerEntity.TextIssuesAnswerEntity) throw InternalServerError("Answer whit id: ${correctAnswer.id} is not compatible with Answer with id: ${correctAnswer.id}")
                            calculateCorrectIssuesCountForTextIssuesAnswer(answer, correctAnswer)
                        }
                        
                        is CorrectAnswerEntity.CorrectTrueFalseAnswerEntity -> {
                            if (answer !is AnswerEntity.TrueFalseAnswerEntity) throw InternalServerError("Answer whit id: ${correctAnswer.id} is not compatible with Answer with id: ${correctAnswer.id}")
                            calculateCorrectIssuesCountForTrueFalseAnswer(answer, correctAnswer)
                        }
                        
                        is CorrectAnswerEntity.CorrectMultipleChoiceAnswerEntity -> {
                            if (answer !is AnswerEntity.MultipleChoiceAnswerEntity) throw InternalServerError("Answer whit id: ${correctAnswer.id} is not compatible with Answer with id: ${correctAnswer.id}")
                            calculateCorrectIssuesCountForMultipleChoiceAnswer(answer, correctAnswer)
                        }
                    }
                }
            }
            return correctIssuesCount
        }
        
        private fun calculateCorrectIssuesCountForTextAnswer(
            answer: AnswerEntity.TextAnswerEntity?,
            correctAnswer: CorrectAnswerEntity.CorrectTextAnswerEntity,
        ): Int {
            return if (correctAnswer.text.pureEqual(answer?.text ?: "")) {
                1
            } else {
                0
            }
        }
        
        private fun calculateCorrectIssuesCountForTextIssuesAnswer(
            answer: AnswerEntity.TextIssuesAnswerEntity?,
            correctAnswer: CorrectAnswerEntity.CorrectTextIssuesAnswerEntity,
        ): Int {
            var correctIssuesCount = 0
            correctAnswer.issues.forEachIndexed { index, correctIssue ->
                val answerIssue = answer?.issues?.getOrNull(index)
                if (answerIssue != null) {
                    if (correctIssue.firstOrNull { it.pureEqual(answerIssue) } != null) {
                        correctIssuesCount++
                    }
                }
            }
            return correctIssuesCount
        }
        
        private fun calculateCorrectIssuesCountForTrueFalseAnswer(
            answer: AnswerEntity.TrueFalseAnswerEntity?,
            correctAnswer: CorrectAnswerEntity.CorrectTrueFalseAnswerEntity,
        ): Int {
            var correctIssuesCount = 0
            correctAnswer.issues.forEachIndexed { index, correctIssue ->
                val answerIssue = answer?.issues?.getOrNull(index)
                if (answerIssue != null) {
                    if (correctIssue == answerIssue) {
                        correctIssuesCount++
                    }
                }
            }
            return correctIssuesCount
        }
        
        private fun calculateCorrectIssuesCountForMultipleChoiceAnswer(
            answer: AnswerEntity.MultipleChoiceAnswerEntity?,
            correctAnswer: CorrectAnswerEntity.CorrectMultipleChoiceAnswerEntity,
        ): Int {
            var correctIssuesCount = 0
            correctAnswer.issues.forEach { correctIssue ->
                answer?.issues?.find {
                    it.order == correctIssue.order
                }?.let { answerIssue ->
                    correctIssue.options.forEach { correctOption ->
                        val answerOption = answerIssue.options.find { it == correctOption }
                        if (answerOption != null && answerOption == correctOption) {
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
        
        fun calculateScore(
            correctIssuesCount: Int, sectionType: SectionType, examType: ExamType
        ): Double {
            return when (examType) {
                ExamType.IELTS_ACADEMIC -> calculateAcademicIELTSScore(correctIssuesCount, sectionType)
                ExamType.IELTS_GENERAL -> TODO()
            }
        }
        
        
        private fun calculateAcademicIELTSScore(correctAnswers: Int, sectionType: SectionType): Double {
            return when (sectionType) {
                SectionType.LISTENING -> {
                    when (correctAnswers) {
                        in 39..40 -> 9.0
                        in 37..38 -> 8.5
                        in 35..36 -> 8.0
                        in 33..34 -> 7.5
                        in 30..32 -> 7.0
                        in 27..29 -> 6.5
                        in 23..26 -> 6.0
                        in 19..22 -> 5.5
                        in 15..18 -> 5.0
                        in 13..14 -> 4.5
                        in 10..12 -> 4.0
                        in 8..9 -> 3.5
                        in 6..7 -> 3.0
                        in 4..5 -> 2.5
                        else -> 0.0
                    }
                }
                
                SectionType.READING -> {
                    when (correctAnswers) {
                        in 39..40 -> 9.0
                        in 37..38 -> 8.5
                        in 35..36 -> 8.0
                        in 33..34 -> 7.5
                        in 30..32 -> 7.0
                        in 26..29 -> 6.5
                        in 23..26 -> 6.0
                        in 19..22 -> 5.5
                        in 15..18 -> 5.0
                        in 13..14 -> 4.5
                        in 10..12 -> 4.0
                        in 8..9 -> 3.5
                        in 6..7 -> 3.0
                        in 4..5 -> 2.5
                        in 3..3 -> 2.0
                        else -> 0.0
                    }
                }
                else -> throw InternalServerError("There is no score calculation for $sectionType section type")
            }
        }
        
        fun generateSectionResultMd(
            section: SectionEntity,
            parts: List<PartEntity>,
            questions: List<QuestionEntity>,
            answers: List<AnswerEntity>,
            correctAnswers: List<CorrectAnswerEntity>
        ): String {
            val sb = StringBuilder()
            sb.append("## **${section.sectionType.title()}** Section\n\n")
            sb.append("- **Duration**: ${section.time} seconds\n\n")
            
            var questionNumber = 0
            parts.forEach { part ->
                questions.filter { it.partId == part.id }.forEach { question ->
                    sb.append("### ${if (section.sectionType == SectionType.LISTENING) "Audio" else "Passage"} ${part.order} - Question ${question.order}\n")
                    sb.append("- **Type**: ${question.questionType.title()}\n")
                    sb.append("- **Header**: ${question.header.split("\n").first()}\n")
                    
                    val answer = answers.find {
                        it.sectionOrder == section.order && it.partOrder == part.order && it.questionOrder == question.order
                    }
                    val correctAnswer = correctAnswers.find {
                        it.sectionId == section.order && it.partId == part.order && it.questionId == question.order
                    }
                    val emptyAnswer = "    "
                    if (correctAnswer != null) {
                        when (correctAnswer) {
//                            is AnswerEntity.TextAnswerEntity -> {
//                                questionNumber++
//                                if (answer.text == (correctAnswer as CorrectAnswerEntity.CorrectTextAnswerEntity).text) {
//                                    "Correct"
//                                } else {
//                                    "Incorrect"
//                                }
//                            }
                            is CorrectAnswerEntity.CorrectMultipleChoiceAnswerEntity -> {
                                val headers = listOf("Question Issue", "Your Answer", "Correct Answer")
                                val rows = mutableListOf(listOf<String>()).also { it.removeFirst() }
                                correctAnswer.issues.forEach { correctIssue ->
                                    val answerIssue = (answer as AnswerEntity.MultipleChoiceAnswerEntity?)?.issues?.find {
                                        it.order == correctIssue.order
                                    }
                                    correctIssue.options.forEach { correctOption ->
                                        questionNumber++
                                        val answerOption = answerIssue?.options?.find { it == correctOption }
                                        when (answerOption) {
                                            null -> {
                                                rows.add(listOf(questionNumber.toString(), emptyAnswer, correctOption.highlight("green")))
                                            }
                                            correctOption -> {
                                                rows.add(listOf(questionNumber.toString(), answerOption.highlight("green"), correctOption.highlight("green")))
                                            }
                                            else -> {
                                                rows.add(listOf(questionNumber.toString(), answerOption.highlight("red"), correctOption.highlight("green")))
                                            }
                                        }
                                    }
                                }
                                sb.createMdTable(headers, rows)
                            }
                            is CorrectAnswerEntity.CorrectTrueFalseAnswerEntity -> {
                                val headers = listOf("Question Issue", "Your Answer", "Correct Answer")
                                val rows = mutableListOf(listOf<String>()).also { it.removeFirst() }
                                correctAnswer.issues.forEachIndexed { index, correctIssue ->
                                    questionNumber++
                                    val answerIssue = (answer as AnswerEntity.TrueFalseAnswerEntity?)?.issues?.getOrNull(index)
                                    if (answerIssue != null) {
                                        if (correctIssue == answerIssue) {
                                            rows.add(
                                                listOf(
                                                    questionNumber.toString(),
                                                    answerIssue.title().highlight("green"),
                                                    correctIssue.title().highlight("green")
                                                )
                                            )
                                        } else {
                                            rows.add(
                                                listOf(
                                                    questionNumber.toString(),
                                                    answerIssue.title().highlight("red"),
                                                    correctIssue.title().highlight("green")
                                                )
                                            )
                                        }
                                    } else {
                                        rows.add(
                                            listOf(
                                                questionNumber.toString(),
                                                emptyAnswer ,
                                                correctIssue.title().highlight("green")
                                            )
                                        )
                                    }
                                }
                                sb.createMdTable(headers, rows)
                            }
                            is CorrectAnswerEntity.CorrectTextIssuesAnswerEntity -> {
                                val headers = listOf("Question Issue", "Your Answer", "Correct Answer")
                                val rows = mutableListOf(listOf<String>()).also { it.removeFirst() }
                                correctAnswer.issues.forEachIndexed { index, correctIssue ->
                                    questionNumber++
                                    val answerIssue = (answer as AnswerEntity.TextIssuesAnswerEntity?)?.issues?.getOrNull(index)
                                    if (answerIssue == null) {
                                        rows.add(
                                            listOf(
                                                questionNumber.toString(),
                                                emptyAnswer,
                                                correctIssue.joinToString().highlight("green")
                                            )
                                        )
                                    }
                                    else if (correctIssue.firstOrNull { it.pureEqual(answerIssue) } != null) {
                                        rows.add(
                                            listOf(
                                                questionNumber.toString(),
                                                answerIssue.highlight("green"),
                                                correctIssue.joinToString().highlight("green")
                                            )
                                        )
                                    } else {
                                        rows.add(
                                            listOf(
                                                questionNumber.toString(),
                                                answerIssue.highlight("red"),
                                                correctIssue.joinToString().highlight("green")
                                            )
                                        )
                                    }
                                }
                                sb.createMdTable(headers, rows)
                            }
                            else -> sb.append("\nInvalid Answer Type\n")
                        }
                    } else {
                        sb.append("\nCorrect Answer Missing\n")
                    }
                    
                    
                    sb.append("\n---\n")
                }
                sb.append("\n")
            }
            return sb.toString()
        }
        
        private fun String.highlight(color: String): String {
            return """ <highlight text="$this" isHighlighted="true" color="$color" showEditIcon="false" /> """
        }
        
        private fun StringBuilder.createMdTable(
            headers: List<String>,
            rows: List<List<String>>
        ): StringBuilder {
            
            val columnWidths = List(headers.size) { index -> index to headers[index].length }.toMap()
            
            this.append("| ")
            headers.forEachIndexed { index, header ->
                this.append(header.padEnd(columnWidths[index] ?: 10)).append(" | ")
            }
            this.append("\n")
            
            this.append("|-")
            columnWidths.forEach { (_, width) ->
                this.append("-".repeat(width)).append("-|")
            }
            this.append("\n")
            
            rows.forEach { row ->
                this.append("| ")
                row.forEachIndexed { index, cell ->
                    this.append(cell.padEnd(columnWidths[index] ?: 10)).append(" | ")
                }
                this.append("\n")
            }
            return this
        }
    }
}
