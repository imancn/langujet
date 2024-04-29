package com.cn.langujet.domain.exam.repository.dto

import com.cn.langujet.domain.exam.model.SectionType

data class SectionMetaDTO(
    var id: String?,
    var examId: String,
    var header: String,
    var order: Int,
    var sectionType: SectionType,
    var time: Long
)
