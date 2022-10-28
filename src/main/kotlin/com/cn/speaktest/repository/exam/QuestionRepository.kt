package com.cn.speaktest.repository.exam

import com.cn.speaktest.model.Question
import org.springframework.data.mongodb.repository.MongoRepository

interface QuestionRepository : MongoRepository<Question, String> {
    fun findAllBySection(section: Question.Section): List<Question>
    fun findAllByTopic(topic: String): List<Question>
}