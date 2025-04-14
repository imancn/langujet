package com.cn.langujet.application.arch.log

import com.cn.langujet.application.arch.advice.HttpException
import com.cn.langujet.application.arch.mongo.models.HistoricalEntity
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

interface LoggerService {
    fun log(msg: String): Boolean
    fun error(exp: HttpException): Boolean
    fun error(exp: RuntimeException): Boolean
    fun logChanges(old: HistoricalEntity<*>, new: HistoricalEntity<*>): Boolean
    fun path(): String {
        val attrs = RequestContextHolder.getRequestAttributes() as? ServletRequestAttributes
        return attrs?.request?.requestURI ?: "unknown"
    }
}
