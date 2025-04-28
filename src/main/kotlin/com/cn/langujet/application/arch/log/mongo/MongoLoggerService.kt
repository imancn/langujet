package com.cn.langujet.application.arch.log.mongo

import com.cn.langujet.application.arch.BundleService
import com.cn.langujet.application.arch.advice.HttpException
import com.cn.langujet.application.arch.log.LoggerService
import com.cn.langujet.application.arch.log.mongo.models.LogChange
import com.cn.langujet.application.arch.log.mongo.models.LogError
import com.cn.langujet.application.arch.log.mongo.models.LogHttpError
import com.cn.langujet.application.arch.log.mongo.models.LogInfo
import com.cn.langujet.application.arch.log.mongo.services.LogChangeService
import com.cn.langujet.application.arch.log.mongo.services.LogErrorService
import com.cn.langujet.application.arch.log.mongo.services.LogHttpErrorService
import com.cn.langujet.application.arch.log.mongo.services.LogInfoService
import com.cn.langujet.application.arch.models.entity.Entity
import com.cn.langujet.application.arch.models.entity.HistoricalEntity
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class MongoLoggerService(
    @Autowired private val bundle: BundleService,
    @Autowired private val mapper: ObjectMapper,
    
    @Autowired private val infoService: LogInfoService,
    @Autowired private val errorService: LogErrorService,
    @Autowired private val changeService: LogChangeService,
    @Autowired private val httpErrorService: LogHttpErrorService
) : LoggerService {
    private final val message = "Unknown error"
    
    override fun log(msg: String) {
        infoService.create(
            LogInfo(
                message = msg,
                path = path()
            )
        )
    }
    
    override fun error(exp: HttpException) {
        httpErrorService.create(
            LogHttpError(
                key = exp.key,
                status = exp.httpStatus.value(),
                message = exp.getMessage(),
                stackTrace = exp.stackTraceToString(),
                path = path()
            )
        )
    }
    
    override fun error(exp: Exception) {
        errorService.create(
            LogError(
                key = "exception",
                message = exp.message.toString(),
                stackTrace = exp.stackTraceToString(),
                path = path()
            )
        )
    }
    
    override fun logChanges(old: Entity<*>, new: Entity<*>) {
        try {
            val entityId = (new as? HistoricalEntity)?.id?.toString() ?: "undefined"
            val oldJson = mapper.writeValueAsString(old)
            val newJson = mapper.writeValueAsString(new)
            
            changeService.create(
                LogChange(
                    entityId = entityId,
                    old = oldJson,
                    new = newJson,
                    path = path()
                )
            )
        } catch (e: Exception) {
            error(e)
        }
    }
    
    private fun HttpException.getMessage(): String = bundle.getMessageResponse(this.key, *this.args).message
}