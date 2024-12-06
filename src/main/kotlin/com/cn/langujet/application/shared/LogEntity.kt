package com.cn.langujet.application.shared

import com.cn.langujet.actor.util.Auth
import java.util.*

open class LogEntity(
    var createdBy: String = Auth.userId(),
    var createdAt: Date = Date(),
) {
    fun setLog(){
        this.createdBy = Auth.userId()
        this.createdAt = Date()
    }
}
