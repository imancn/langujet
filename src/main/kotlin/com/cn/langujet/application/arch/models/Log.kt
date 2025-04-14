package com.cn.langujet.application.arch.models

import com.cn.langujet.actor.util.Auth
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import java.util.*

abstract class Log {
    @field:CreatedBy
    var createdBy: String = "undefined"
    @field:CreatedDate
    var createdAt: Date = Date(0)
    
    fun setLog(){
        this.createdBy = userId()
        this.createdAt = Date()
    }
    
    private fun userId() = Auth.userId()
}