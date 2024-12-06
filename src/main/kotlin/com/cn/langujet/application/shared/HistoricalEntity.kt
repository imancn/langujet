package com.cn.langujet.application.shared

import com.cn.langujet.actor.util.Auth
import java.util.*

open class HistoricalEntity(
    var updatedBy: String = Auth.userId(),
    var updatedAt: Date = Date(),
): LogEntity() {
    
    fun updateLog() {
        this.also {
            updatedAt = Date()
            updatedBy = Auth.userId()
        }
    }
}
