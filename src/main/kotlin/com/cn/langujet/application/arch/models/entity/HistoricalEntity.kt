package com.cn.langujet.application.arch.models.entity

import com.cn.langujet.actor.util.Auth
import org.springframework.data.annotation.*
import java.util.*

abstract class HistoricalEntity(id: Long?) : Entity<Long>(id) {
    @field:Version
    var version: Long = 0
    
    var deleted: Boolean = false
    
    @field:CreatedBy
    var createdBy: Long = Auth.UNDEFINED
    @field:CreatedDate
    var createdAt: Date = Date(0)
    
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
    
    fun setLog(){
        this.createdBy = userId()
        this.createdAt = Date()
    }
    
    private fun userId() = Auth.userId()
}
