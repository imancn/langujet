package com.cn.langujet.application.arch.log.mongo

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
import java.text.MessageFormat
import java.util.*

@Service
class MongoLoggerService(
    @Autowired private val bundle: ResourceBundle,
    @Autowired private val mapper: ObjectMapper,
    
    @Autowired private val infoService: LogInfoService,
    @Autowired private val errorService: LogErrorService,
    @Autowired private val changeService: LogChangeService,
    @Autowired private val httpErrorService: LogHttpErrorService
) : LoggerService {
    private final val message = "Unknown error"
    
    override fun log(msg: String): String {
        return try {
            infoService.create(
                LogInfo(
                    message = msg,
                    path = path()
                )
            ).message
        } catch (e: Exception) {
            e.message ?: message
        }
    }
    
    override fun error(exp: HttpException): String {
        return try {
            httpErrorService.create(
                LogHttpError(
                    key = exp.key,
                    status = exp.httpStatus.value(),
                    message = exp.getMessage(),
                    stackTrace = exp.stackTraceToString(),
                    path = path()
                )
            ).message
        } catch (e: Exception) {
            e.message ?: message
        }
    }
    
    override fun error(exp: Exception): String {
        return try {
            errorService.create(
                LogError(
                    key = "exception",
                    message = exp.message.toString(),
                    stackTrace = exp.stackTraceToString(),
                    path = path()
                )
            ).message
        } catch (e: Exception) {
            e.message ?: message
        }
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
    
    private fun HttpException.getMessage(): String = try {
        bundle.getString(this.key).let { template ->
            MessageFormat.format(template, *this.args)
        }
    } catch (_: Exception) {
        this.key
    }
}