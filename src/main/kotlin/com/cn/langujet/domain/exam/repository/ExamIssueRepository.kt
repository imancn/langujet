package com.cn.langujet.domain.exam.repository

import com.cn.langujet.domain.exam.model.ExamIssue
import org.springframework.data.mongodb.repository.MongoRepository

interface ExamIssueRepository : MongoRepository<ExamIssue, String> {
    fun findByAnswerId(id: String): ExamIssue?
}