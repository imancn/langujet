package com.cn.langujet.application.arch.models.entity

import com.cn.langujet.actor.util.Auth
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import java.util.*

abstract class LogEntity : Entity<String>() {
    @field:CreatedBy
    var createdBy: Long = Auth.UNDEFINED
    
    @field:CreatedDate
    var createdAt: Date = Date(0)
    
    fun setLog() {
        this.createdBy = userId()
        this.createdAt = Date()
    }
    
    private fun userId() = Auth.userId()
}