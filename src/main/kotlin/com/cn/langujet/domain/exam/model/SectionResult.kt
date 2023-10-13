package com.cn.langujet.domain.exam.model

import com.cn.langujet.actor.exam.payload.SectionResultDto

class SectionResult(
    var sectionId: String,
    var sectionType: SectionType,
    var score: Int,
    var recommendation: String,
) {
    constructor(sectionResultDto: SectionResultDto) : this(
        sectionResultDto.sectionId,
        sectionResultDto.sectionType,
        sectionResultDto.score,
        sectionResultDto.recommendation
    )
}