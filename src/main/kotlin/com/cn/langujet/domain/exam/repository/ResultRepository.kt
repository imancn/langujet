package com.cn.langujet.domain.exam.repository

import com.cn.langujet.domain.exam.model.Result
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.Optional

interface ResultRepository : MongoRepository<Result, String> {
    fun findByExamSessionId(examSessionId: String): Optional<Result>
}