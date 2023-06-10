package com.cn.speaktest.actor.exam.payload.dto

import com.cn.speaktest.domain.exam.model.Section

class SectionDto(
    var id: String?,
    var examId: String,
    var name: String?,
    var order: Int?,
) {

    constructor(section: Section) : this(
        section.id,
        section.examId,
        section.name,
        section.order,
    )
}
