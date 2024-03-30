package com.cn.langujet.domain.exam.repository

import com.cn.langujet.domain.exam.model.ExamSession
import com.cn.langujet.domain.exam.model.ExamSessionState
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.Optional

interface ExamSessionRepository : MongoRepository<ExamSession, String> {
    fun findByStudentIdAndState(s: String, started: ExamSessionState): Optional<ExamSession>
}