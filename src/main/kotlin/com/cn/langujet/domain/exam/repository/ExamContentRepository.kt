package com.cn.langujet.domain.exam.repository

import com.cn.langujet.domain.exam.model.ExamContentEntity
import org.springframework.data.mongodb.repository.MongoRepository

interface ExamContentRepository : MongoRepository<ExamContentEntity, String> {
    fun findAllByExamIdAndSectionOrder(examId: String, sectionOrder: Int): List<ExamContentEntity>
}