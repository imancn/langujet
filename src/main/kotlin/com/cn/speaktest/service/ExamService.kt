package com.cn.speaktest.service

import com.cn.speaktest.model.ExamIssue
import com.cn.speaktest.model.Question
import com.cn.speaktest.repository.exam.ExamIssueRepository
import com.cn.speaktest.repository.exam.QuestionRepository
import org.springframework.stereotype.Service

@Service
class ExamService(
    val questionRepository: QuestionRepository,
    val examIssueRepository: ExamIssueRepository
) {
    fun generateExamIssueList(examId: String): List<ExamIssue> {
        val examIssues = mutableListOf<ExamIssue>().toMutableList()
        val usedQuestions = mutableListOf<Question>().toMutableList()
        Question.Section.values().sortedBy { it.num }.forEach { section ->
            val topic = questionRepository.findAllBySection(section).random().topic
            val questions = questionRepository.findAllByTopic(topic).sortedBy { it.order }

            questions.forEach { question ->
                examIssues.add(
                    examIssueRepository.save(
                        ExamIssue(
                            examId,
                            question,
                            examIssues.size
                        )
                    )
                )
            }
            usedQuestions.addAll(questions)
        }
        questionRepository.saveAll(
            usedQuestions.onEach {
                it.usageNumber = it.usageNumber + 1
            }
        )
        return examIssues
    }

}
