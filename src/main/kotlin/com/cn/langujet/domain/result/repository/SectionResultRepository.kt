package com.cn.langujet.domain.result.repository

import com.cn.langujet.domain.result.model.SectionResult
import org.springframework.data.mongodb.repository.MongoRepository

interface SectionResultRepository : MongoRepository<SectionResult, String> {
    fun findAllByResultId(resultId: String): List<SectionResult>
}