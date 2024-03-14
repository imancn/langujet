package com.cn.langujet.domain.answer

import com.cn.langujet.domain.answer.model.Answer
import org.springframework.data.mongodb.repository.MongoRepository

interface AnswerRepository : MongoRepository<Answer, String> {
    fun findAllByExamSessionIdAndSectionOrder(examSessionId: String, sectionOrder: Int): List<Answer>
    
    fun existsByExamSessionIdAndSectionOrderAndPartOrderAndQuestionOrder(
        examSessionId: String,
        sectionOrder: Int,
        partOrder: Int,
        questionOrder: Int,
    ): Boolean
}