package com.cn.langujet.domain.exam.repository

import com.cn.langujet.application.arch.mongo.HistoricalMongoRepository
import com.cn.langujet.domain.exam.model.ExamContentEntity

interface ExamContentRepository : HistoricalMongoRepository<ExamContentEntity> {
    fun findAllByExamIdAndSectionOrder(examId: Long, sectionOrder: Int): List<ExamContentEntity>
}