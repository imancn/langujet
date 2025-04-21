package com.cn.langujet.domain.exam.repository

import com.cn.langujet.domain.exam.model.ExamContentEntity
import org.springframework.data.mongodb.repository.MongoRepository

interface ExamContentRepository : MongoRepository<ExamContentEntity, Long> {
    fun findAllByExamIdAndSectionOrder(examId: Long, sectionOrder: Int): List<ExamContentEntity>
}