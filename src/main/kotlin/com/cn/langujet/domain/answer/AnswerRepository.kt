package com.cn.langujet.domain.answer

import com.cn.langujet.domain.answer.model.Answer
import org.springframework.data.mongodb.repository.MongoRepository

interface AnswerRepository : MongoRepository<Answer, String> {
    fun findByExamIssueId(examIssueId: String): List<Answer>
}