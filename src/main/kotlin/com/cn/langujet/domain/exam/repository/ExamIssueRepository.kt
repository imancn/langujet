package com.cn.langujet.domain.exam.repository

import com.cn.langujet.domain.exam.model.ExamIssue
import org.springframework.data.mongodb.repository.MongoRepository

interface ExamIssueRepository : MongoRepository<ExamIssue, String> {
    fun findByExamSessionId(id: String): List<ExamIssue>
    fun findByAnswerId(id: String): ExamIssue?
}