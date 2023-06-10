package com.cn.speaktest.domain.question

import com.cn.speaktest.domain.answer.model.AnswerType
import com.cn.speaktest.domain.exam.model.Section
import com.cn.speaktest.domain.question.model.Question
import com.cn.speaktest.domain.question.model.QuestionType
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query

interface QuestionRepository : MongoRepository<Question, String> {
    fun findAllBySection(section: Section): List<Question>
    fun findAllByTopic(topic: String): List<Question>

    fun findAllByQuestionType(questionType: QuestionType): List<Question>

    fun findAllByAnswerType(answerType: AnswerType): List<Question>

    @Query("{'id': ?0, 'exam.id': ?1, 'topic': { \$regex: ?2, \$options: 'i' }, 'section': ?3, 'order': ?4, 'answerType': ?5, 'questionType': ?6}")
    fun findAllQuestionsByFilters(
        id: String?,
        examId: String?,
        topic: String?,
        section: Int?,
        order: Int?,
        answerType: AnswerType?,
        questionType: QuestionType?,
        pageRequest: PageRequest
    ): Page<Question>

    fun existsByExam_IdAndSection_IdAndTopicAndOrder(examId: String?, section: String?, topic: String, order: Int): Boolean
}