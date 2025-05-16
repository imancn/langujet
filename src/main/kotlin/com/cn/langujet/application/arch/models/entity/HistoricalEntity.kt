package com.cn.langujet.application.arch.models.entity

import com.cn.langujet.application.service.users.Auth
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.annotation.*
import java.util.*

abstract class HistoricalEntity(
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    id: Long?
) : Entity<Long>(id) {
    @field:Version
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    open var version: Long = 0
    
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
            updatedBy = Auth.userId()
            updatedAt = Date()
        }
    }
    
    fun setLog(){
        this.createdBy = Auth.userId()
        this.createdAt = Date()
    }
}
