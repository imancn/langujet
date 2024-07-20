package com.cn.langujet.domain.exam.repository

import com.cn.langujet.domain.exam.model.ExamSectionContentEntity
import org.springframework.data.mongodb.repository.MongoRepository

interface ExamSectionContentRepository : MongoRepository<ExamSectionContentEntity, String> {
    fun findAllByExamIdAndSectionOrder(examId: String, sectionOrder: Int): List<ExamSectionContentEntity>
}