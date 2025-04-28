package com.cn.langujet.application.arch.models

import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import java.util.*

abstract class Historical : Log() {
    @field:LastModifiedBy
    var updatedBy: Long? = null
    @field:LastModifiedDate
    var updatedAt: Date? = null
}