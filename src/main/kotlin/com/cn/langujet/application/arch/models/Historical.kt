package com.cn.langujet.application.arch.models

import com.cn.langujet.actor.util.Auth
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import java.util.*

abstract class Historical : Log() {
    @field:LastModifiedBy
    var updatedBy: Long = Auth.UNDEFINED
    @field:LastModifiedDate
    var updatedAt: Date = Date(0)
    
    fun updateLog() {
        this.also {
            updatedBy = userId()
            updatedAt = Date()
        }
    }
    
    private fun userId() = Auth.userId()
}