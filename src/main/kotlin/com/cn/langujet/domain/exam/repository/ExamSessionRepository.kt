package com.cn.langujet.domain.exam.repository

import com.cn.langujet.domain.exam.model.ExamSession
import com.cn.langujet.domain.exam.model.ExamSessionState
import org.springframework.data.mongodb.repository.MongoRepository

interface ExamSessionRepository : MongoRepository<ExamSession, String> {
    fun findByStudentUserIdAndState(userId: String, started: ExamSessionState): List<ExamSession>
    fun findByStudentUserId(userId: String): List<ExamSession>
}