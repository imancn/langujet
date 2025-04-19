package com.cn.langujet.application.arch.log.mongo.models

import com.cn.langujet.application.arch.models.entity.LogEntity
import org.springframework.data.mongodb.core.mapping.Document

@Document("logs_errors")
data class LogHttpError(
    val status: Int,
    val key: String,
    val message: String,
    val stackTrace: String,
    val path: String
) : LogEntity()