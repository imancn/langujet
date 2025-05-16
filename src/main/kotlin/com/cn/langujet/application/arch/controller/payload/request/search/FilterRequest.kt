package com.cn.langujet.application.arch.controller.payload.request.search

import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query

class FilterRequest(
    private val expressions: List<Expression>
) {
    fun query(): Query {
        return Query(
            Criteria().andOperator(
                *(expressions).map {
                    if (it.values.size > 1) { Criteria(it.key).`in`(it.values) }
                    else if (it.values.size == 1) { Criteria(it.key).`in`(it.values[0]) }
                    else { return Query() }
                }.toTypedArray()
            )
        )
    }
}

class Expression(
    val key: String, val values: List<Any>
)