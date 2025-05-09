package com.cn.langujet.application.arch.log

import com.cn.langujet.application.arch.advice.HttpException
import com.cn.langujet.application.arch.models.entity.Entity
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

interface LoggerService {
    fun log(msg: String)
    fun error(exp: HttpException)
    fun error(exp: Exception)
    fun logChanges(old: Entity<*>, new: Entity<*>)
    fun path(): String {
        val attrs = RequestContextHolder.getRequestAttributes() as? ServletRequestAttributes
        return attrs?.request?.requestURI ?: "unknown"
    }
}
