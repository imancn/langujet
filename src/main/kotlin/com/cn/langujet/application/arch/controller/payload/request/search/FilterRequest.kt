package com.cn.langujet.application.arch.controller.payload.request.search

import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query

class FilterRequest(
    val expressions: List<Expression>
) {
    fun query(): Query {
        if (expressions.isEmpty()) return Query()
        return Query(
            Criteria().andOperator(
                *expressions.map {
                    Criteria(it.key).`in`(it.values)
                }.toTypedArray()
            )
        )
    }
}

class Expression(
    val key: String, val values: List<Any>
)