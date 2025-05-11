package com.cn.langujet.application.arch.models.entity

import com.cn.langujet.application.arch.advice.InternalServerError
import org.springframework.data.annotation.Id

abstract class Entity<ID : Any>(
    @field:Id open var id: ID? = null
): EntityInterface<ID> {

    override fun id(): ID {
        return id ?: throw InternalServerError("used.null.id", this.javaClass)
    }

    override fun id(id: ID?) {
        this.id = id
    }
    
    companion object {
        const val UNKNOWN_ID: Long = 0
    }
}
