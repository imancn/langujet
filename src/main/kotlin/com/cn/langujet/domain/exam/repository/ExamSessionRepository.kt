package com.cn.langujet.domain.exam.repository

import com.cn.langujet.application.arch.mongo.HistoricalMongoRepository
import com.cn.langujet.domain.exam.model.ExamSessionEntity
import com.cn.langujet.domain.exam.model.enums.ExamSessionState

interface ExamSessionRepository : HistoricalMongoRepository<ExamSessionEntity> {
    fun findByStudentUserIdAndState(userId: Long, started: ExamSessionState): List<ExamSessionEntity>
    fun findByStudentUserId(userId: Long): List<ExamSessionEntity>
}