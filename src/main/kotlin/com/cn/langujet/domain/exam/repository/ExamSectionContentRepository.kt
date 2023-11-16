package com.cn.langujet.domain.exam.repository

import com.cn.langujet.domain.exam.model.ExamSectionContent
import org.springframework.data.mongodb.repository.MongoRepository

interface ExamSectionContentRepository : MongoRepository<ExamSectionContent, String> {
    fun findAllByExamIdAndSectionOrder(examId: String, sectionOrder: Int): List<ExamSectionContent>
}