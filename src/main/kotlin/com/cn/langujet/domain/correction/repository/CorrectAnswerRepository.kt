package com.cn.langujet.domain.correction.repository

import com.cn.langujet.domain.correction.model.CorrectAnswerEntity
import org.springframework.data.mongodb.repository.MongoRepository

interface CorrectAnswerRepository : MongoRepository<CorrectAnswerEntity, Long> {
    fun existsByExamIdAndSectionOrder(examId: Long, sectionOrder: Int): Boolean
    fun findAllByExamIdAndSectionOrder(examId: Long, sectionOrder: Int): List<CorrectAnswerEntity>
    fun existsByExamIdAndSectionOrderAndPartOrderAndQuestionOrder(
        examId: Long,
        sectionOrder: Int,
        partOrder: Int,
        questionOrder: Int
    ): Boolean
}