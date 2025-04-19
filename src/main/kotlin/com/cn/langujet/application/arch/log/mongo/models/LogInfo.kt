package com.cn.langujet.application.arch.log.mongo.models

import com.cn.langujet.application.arch.models.entity.LogEntity
import org.springframework.data.mongodb.core.mapping.Document

@Document("logs")
data class LogInfo(
    val message: String,
    val path: String
) : LogEntity()