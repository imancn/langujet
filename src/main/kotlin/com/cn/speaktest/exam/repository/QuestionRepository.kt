package com.cn.speaktest.exam.repository

import com.cn.speaktest.exam.model.Question
import org.springframework.data.mongodb.repository.MongoRepository

interface QuestionRepository : MongoRepository<Question, String> {
    fun findAllBySection(section: Question.Section): List<Question>
    fun findAllByTopic(topic: String): List<Question>
    fun existsBySectionAndTopicAndOrder(section: Question.Section, topic: String, order: Int): Boolean
}