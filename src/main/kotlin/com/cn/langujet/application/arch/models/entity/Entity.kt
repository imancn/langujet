package com.cn.langujet.application.arch.models.entity

import org.springframework.data.annotation.Id

abstract class Entity<ID>(
    @field:Id open var id: ID? = null
): EntityInterface<ID> {
    
    override fun id(): ID? {
        return id
    }
    
    override fun id(id: ID?) {
        this.id = id
    }
    
    companion object {
        const val UNKNOWN_ID: Long = 0
    }
}
