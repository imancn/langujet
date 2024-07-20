package com.cn.langujet.domain.correction.repository

import com.cn.langujet.domain.correction.model.CorrectAnswerEntity
import org.springframework.data.mongodb.repository.MongoRepository

interface CorrectAnswerRepository : MongoRepository<CorrectAnswerEntity, String> {
    fun existsByExamIdAndSectionOrder(examId: String, sectionOrder: Int): Boolean
    fun findAllByExamIdAndSectionOrder(examId: String, sectionOrder: Int): List<CorrectAnswerEntity>
}