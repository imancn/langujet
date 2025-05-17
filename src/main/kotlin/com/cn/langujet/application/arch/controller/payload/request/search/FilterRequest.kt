package com.cn.langujet.application.arch.controller.payload.request.search

import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query

data class FilterRequest(
    val expressions: List<Expression>
) {
    fun query(): Query {
        val criteriaList = (expressions).mapNotNull {
            if (it.values.size > 1) {
                Criteria(it.key).`in`(it.values)
            } else if (it.values.size == 1) {
                Criteria(it.key).`is`(it.values[0])
            } else {
                null
            }
        }
        if (criteriaList.isEmpty()) return Query()
        return Query(Criteria().andOperator(*criteriaList.toTypedArray()))
    }
}

class Expression(
    val key: String, val values: List<Any>
)