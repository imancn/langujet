package com.cn.langujet.domain.answer

import com.cn.langujet.domain.answer.model.AnswerEntity
import org.springframework.data.mongodb.repository.MongoRepository

interface AnswerRepository : MongoRepository<AnswerEntity, String> {
    fun findAllByExamSessionIdAndSectionOrder(examSessionId: String, sectionOrder: Int): List<AnswerEntity>
    
    fun existsByExamSessionIdAndSectionOrderAndPartOrderAndQuestionOrder(
        examSessionId: String,
        sectionOrder: Int,
        partOrder: Int,
        questionOrder: Int,
    ): Boolean
}