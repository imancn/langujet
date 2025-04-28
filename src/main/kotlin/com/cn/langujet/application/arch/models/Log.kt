package com.cn.langujet.application.arch.models

import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import java.util.*

abstract class Log {
    @field:CreatedBy
    val createdBy: Long? = null
    @field:CreatedDate
    val createdAt: Date? = null
}