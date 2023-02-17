package com.cn.speaktest.exam.service

import com.cn.speaktest.exam.api.ExamService
import com.cn.speaktest.exam.model.ExamIssue
import com.cn.speaktest.exam.model.Question
import com.cn.speaktest.exam.repository.ExamIssueRepository
import com.cn.speaktest.exam.repository.QuestionRepository
import org.springframework.stereotype.Service

@Service
class ExamServiceImpl(
    val questionRepository: QuestionRepository,
    val examIssueRepository: ExamIssueRepository
): ExamService {
    override fun generateExamIssueList(examId: String): List<ExamIssue> {
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
