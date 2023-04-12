package com.cn.speaktest.answer

import com.cn.speaktest.answer.model.Answer
import org.springframework.data.mongodb.repository.MongoRepository

interface AnswerRepository : MongoRepository<Answer, String> {
    fun findByExamIssueId(examIssueId: String): List<Answer>
}