package com.cn.langujet.domain.correction.repository

import com.cn.langujet.domain.correction.model.CorrectAnswerEntity
import org.springframework.data.mongodb.core.MongoOperations
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Component

@Component
class CorrectAnswerCustomRepository(
    private val mongo: MongoOperations
) {
    fun findCorrectAnswersByOptionalCriteria(
        examId: String,
        sectionOrder: Int,
        partOrder: Int?,
        questionOrder: Int?
    ): List<CorrectAnswerEntity> {
        val query = Query()
        query.addCriteria(Criteria.where("examId").`is`(examId))
        query.addCriteria(Criteria.where("sectionOrder").`is`(sectionOrder))
        partOrder?.let { query.addCriteria(Criteria.where("partOrder").`is`(it)) }
        questionOrder?.let { query.addCriteria(Criteria.where("questionOrder").`is`(it)) }
        return mongo.find(query, CorrectAnswerEntity::class.java)
    }
}