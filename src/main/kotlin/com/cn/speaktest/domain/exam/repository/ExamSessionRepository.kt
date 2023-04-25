package com.cn.speaktest.domain.exam.repository

import com.cn.speaktest.domain.exam.model.ExamSession
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.*

interface ExamSessionRepository : MongoRepository<ExamSession, String> {
    fun findBySuggestion_Id(suggestionId: String): Optional<ExamSession>

}