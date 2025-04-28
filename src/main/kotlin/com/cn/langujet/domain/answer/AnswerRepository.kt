package com.cn.langujet.domain.answer

import com.cn.langujet.application.arch.mongo.HistoricalMongoRepository
import com.cn.langujet.domain.answer.model.AnswerEntity

interface AnswerRepository : HistoricalMongoRepository<AnswerEntity> {
    fun findAllByExamSessionIdAndSectionOrder(examSessionId: Long, sectionOrder: Int): List<AnswerEntity>
    
    fun existsByExamSessionIdAndSectionOrderAndPartOrderAndQuestionOrder(
        examSessionId: Long,
        sectionOrder: Int,
        partOrder: Int,
        questionOrder: Int,
    ): Boolean
}