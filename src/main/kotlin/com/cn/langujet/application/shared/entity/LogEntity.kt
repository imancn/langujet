package com.cn.langujet.application.shared.entity

import com.cn.langujet.actor.util.Auth
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import java.util.*

open class LogEntity{
    @field:CreatedBy
    var createdBy: String = "undefined"
    @field:CreatedDate
    var createdAt: Date = Date(0)
    
    fun setLog(){
        this.createdBy = Auth.userId()
        this.createdAt = Date()
    }
}
