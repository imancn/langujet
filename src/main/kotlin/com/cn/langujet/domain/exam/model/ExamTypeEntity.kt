package com.cn.langujet.domain.exam.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
class ExamTypeEntity (
    @Id
    var id: String?,

    var name: String,
    var examType: ExamType,
    var sectionType: SectionType?,
    var price: Double,

    var order: Int,
    var active: Boolean
)
