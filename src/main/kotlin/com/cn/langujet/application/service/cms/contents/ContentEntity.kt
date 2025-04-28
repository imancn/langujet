package com.cn.langujet.application.service.cms.contents

import com.cn.langujet.application.arch.models.entity.HistoricalEntity
import com.cn.langujet.application.service.cms.enums.Languages
import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.core.mapping.Document

@TypeAlias(value = "contents")
@Document(collection = "contents")
class ContentEntity(
    id: Long?,
    val key: String,
    val language: Languages,
    val content: String,
): HistoricalEntity(id = id)