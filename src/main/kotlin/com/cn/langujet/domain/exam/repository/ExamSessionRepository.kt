package com.cn.langujet.domain.exam.repository

import com.cn.langujet.domain.exam.model.ExamSessionEntity
import com.cn.langujet.domain.exam.model.enums.ExamSessionState
import org.springframework.data.mongodb.repository.MongoRepository

interface ExamSessionRepository : MongoRepository<ExamSessionEntity, String> {
    fun findByStudentUserIdAndState(userId: String, started: ExamSessionState): List<ExamSessionEntity>
    fun findByStudentUserId(userId: String): List<ExamSessionEntity>
}