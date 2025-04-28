package com.cn.langujet.application.arch.models.entity

import com.cn.langujet.actor.util.Auth
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.annotation.*
import java.util.*

abstract class HistoricalEntity(id: Long?) : Entity<Long>(id) {
    @field:Version
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    open var version: Long = 0
    
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    open var deleted: Boolean = false
    
    @field:CreatedBy
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    open var createdBy: Long? = null
    
    @field:CreatedDate
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    open var createdAt: Date? = null
    
    @field:LastModifiedBy
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    open var updatedBy: Long? = null
    
    @field:LastModifiedDate
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    open var updatedAt: Date? = null
    
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
