package com.cn.langujet.actor.exam.payload

import com.cn.langujet.domain.exam.model.ExamTypeEntity

class ExamTypeResponse (
    var id: String,
    var name: String,
    var price: Double
) {

    companion object {
        fun ExamTypeEntity.toExamTypeResponse(): ExamTypeResponse {
            return ExamTypeResponse(
                id!!,
                name,
                price,
            )
        }
    }
}
