package com.cn.langujet.domain.correction.repository

import com.cn.langujet.application.arch.mongo.HistoricalMongoRepository
import com.cn.langujet.domain.correction.model.CorrectAnswerEntity

interface CorrectAnswerRepository : HistoricalMongoRepository<CorrectAnswerEntity> {
    fun existsByExamIdAndSectionId(examId: Long, sectionId: Int): Boolean
    fun findAllByExamIdAndSectionId(examId: Long, sectionId: Int): List<CorrectAnswerEntity>
    fun existsByExamIdAndSectionIdAndPartIdAndQuestionId(
        examId: Long,
        sectionId: Int,
        partId: Int,
        questionId: Int
    ): Boolean
}