package com.cn.langujet.domain.answer

import com.cn.langujet.domain.answer.model.AnswerEntity
import org.springframework.data.mongodb.repository.MongoRepository

interface AnswerRepository : MongoRepository<AnswerEntity, Long> {
    fun findAllByExamSessionIdAndSectionOrder(examSessionId: Long, sectionOrder: Int): List<AnswerEntity>
    
    fun existsByExamSessionIdAndSectionOrderAndPartOrderAndQuestionOrder(
        examSessionId: Long,
        sectionOrder: Int,
        partOrder: Int,
        questionOrder: Int,
    ): Boolean
}