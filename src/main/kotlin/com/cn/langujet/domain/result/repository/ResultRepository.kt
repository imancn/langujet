package com.cn.langujet.domain.result.repository

import com.cn.langujet.domain.result.model.Result
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.*

interface ResultRepository : MongoRepository<Result, String> {
    fun findByExamSessionId(examSessionId: String): Optional<Result>
}