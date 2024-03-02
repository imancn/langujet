package com.cn.langujet.domain.correction.repository

import com.cn.langujet.domain.correction.model.CorrectAnswer
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
    ): List<CorrectAnswer> {
        val query = Query()
        query.addCriteria(Criteria.where("examId").`is`(examId))
        query.addCriteria(Criteria.where("sectionOrder").`is`(sectionOrder))
        partOrder?.let { query.addCriteria(Criteria.where("partOrder").`is`(it)) }
        questionOrder?.let { query.addCriteria(Criteria.where("questionOrder").`is`(it)) }
        return mongo.find(query, CorrectAnswer::class.java)
    }
}