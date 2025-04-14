package com.cn.langujet.application.arch.log

import com.cn.langujet.application.arch.models.Entity
import com.cn.langujet.application.arch.models.Log
import org.springframework.data.annotation.Id

abstract class LogEntity : Log(), Entity {
    @field:Id
    open var id: String? = null
}
