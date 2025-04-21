package com.cn.langujet.domain.exam.repository.dto

import com.cn.langujet.domain.exam.model.enums.SectionType

data class SectionMetaDTO(
    var id: Long?,
    var examId: Long,
    var header: String,
    var order: Int,
    var sectionType: SectionType,
    var time: Long
)
