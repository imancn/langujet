package com.cn.speaktest.exam.repository

import com.cn.speaktest.exam.model.ExamSession
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.Optional

interface ExamSessionRepository : MongoRepository<ExamSession, String> {
    fun findBySuggestion_Id(suggestionId: String): Optional<ExamSession>

}