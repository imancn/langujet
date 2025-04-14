package com.cn.langujet.application.arch.mongo.models

import com.cn.langujet.application.arch.models.Entity
import com.cn.langujet.application.arch.models.Historical
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Version

abstract class HistoricalEntity<T> : Historical(), Entity {
    @field:Id
    var id: T? = null
    
    @field:Version
    var version: Long = 0
    
    var deleted: Boolean = false
}
