package com.cn.langujet.domain.exam.repository

import com.cn.langujet.domain.exam.repository.dto.SectionMetaDTO
import org.springframework.data.mongodb.core.MongoOperations
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Component

@Component
class SectionCustomRepository(
    private val mongoOperations: MongoOperations,
) {
    fun findSectionsMetaDataByExamId(examId: String): List<SectionMetaDTO> {
        val query = Query()
        query.addCriteria(Criteria.where("examId").`is`(examId))
        query.fields().exclude("parts")
        return mongoOperations.find(query, SectionMetaDTO::class.java, "sections")
    }
}