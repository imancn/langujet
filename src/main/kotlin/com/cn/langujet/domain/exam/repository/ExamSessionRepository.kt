package com.cn.langujet.domain.exam.repository

import com.cn.langujet.domain.exam.model.ExamSession
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.*

interface ExamSessionRepository : MongoRepository<ExamSession, String> {
    fun findBySuggestion_Id(suggestionId: String): Optional<ExamSession>

}