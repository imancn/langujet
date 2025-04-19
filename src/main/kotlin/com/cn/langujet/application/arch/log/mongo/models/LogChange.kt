package com.cn.langujet.application.arch.log.mongo.models

import com.cn.langujet.application.arch.models.entity.LogEntity
import org.springframework.data.mongodb.core.mapping.Document

@Document("logs_changes")
data class LogChange(
    val entityId: String,
    val old: String,
    val new: String,
    val path: String
) : LogEntity()