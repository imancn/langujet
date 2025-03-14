package com.cn.langujet.application.shared.entity

import com.cn.langujet.actor.util.Auth
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import java.util.*

open class HistoricalEntity: LogEntity() {
    @field:LastModifiedBy
    var updatedBy: String = "undefined"
    @field:LastModifiedDate
    var updatedAt: Date = Date(0)
    
    fun updateLog() {
        this.also {
            updatedAt = Date()
            updatedBy = Auth.userId()
        }
    }
}
