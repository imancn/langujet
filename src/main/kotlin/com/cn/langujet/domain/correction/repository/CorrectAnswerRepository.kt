package com.cn.langujet.domain.correction.repository

import com.cn.langujet.application.arch.mongo.HistoricalMongoRepository
import com.cn.langujet.domain.correction.model.CorrectAnswerEntity

interface CorrectAnswerRepository : HistoricalMongoRepository<CorrectAnswerEntity> {
    fun existsByExamIdAndSectionOrder(examId: Long, sectionOrder: Int): Boolean
    fun findAllByExamIdAndSectionOrder(examId: Long, sectionOrder: Int): List<CorrectAnswerEntity>
    fun existsByExamIdAndSectionOrderAndPartOrderAndQuestionOrder(
        examId: Long,
        sectionOrder: Int,
        partOrder: Int,
        questionOrder: Int
    ): Boolean
}