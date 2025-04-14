package com.cn.langujet.application.arch.log

import com.cn.langujet.application.arch.advice.HttpException
import com.cn.langujet.application.arch.mongo.models.HistoricalEntity
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Service

@Service
class MongoLoggerService(
    private val infoRepo: LogInfoRepository,
    private val warningRepo: LogWarningRepository,
    private val errorRepo: LogErrorRepository,
    private val changeRepo: LogChangeRepository
) : LoggerService {
    
    private val mapper = jacksonObjectMapper()
    
    override fun log(msg: String): Boolean {
        return try {
            infoRepo.save(
                LogInfo(
                    message = msg,
                    path = path()
                )
            )
            true
        } catch (e: Exception) {
            false
        }
    }
    
    override fun error(exp: HttpException): Boolean {
        return try {
            errorRepo.save(
                LogError(
                    key = exp.key,
                    message = "HttpException: ${exp.message}, status: ${exp.httpStatus}",
                    stackTrace = exp.stackTraceToString(),
                    path = path()
                )
            )
            true
        } catch (e: Exception) {
            false
        }
    }
    
    override fun error(exp: RuntimeException): Boolean {
        return try {
            errorRepo.save(
                LogError(
                    key = "runtime.exception",
                    message = "RuntimeException: ${exp.message}",
                    stackTrace = exp.stackTraceToString(),
                    path = path()
                )
            )
            true
        } catch (e: Exception) {
            false
        }
    }
    
    override fun logChanges(old: HistoricalEntity<*>, new: HistoricalEntity<*>): Boolean {
        return try {
            val entityId = (new as? HistoricalEntity<*>)?.id?.toString() ?: "undefined"
            val oldJson = mapper.writeValueAsString(old)
            val newJson = mapper.writeValueAsString(new)
            
            changeRepo.save(
                LogChange(
                    entityId = entityId,
                    old = oldJson,
                    new = newJson,
                    path = path()
                )
            )
            true
        } catch (e: Exception) {
            false
        }
    }
    
    
}

interface LogInfoRepository : MongoRepository<LogInfo, String>
interface LogWarningRepository : MongoRepository<LogWarning, String>
interface LogErrorRepository : MongoRepository<LogError, String>
interface LogChangeRepository : MongoRepository<LogChange, String>

@Document("logs")
data class LogInfo(
    override var id: String? = null,
    val message: String,
    val path: String
) : LogEntity()

@Document("warnings")
data class LogWarning(
    val key: String,
    val message: String,
    val stackTrace: String,
    val path: String
) : LogEntity()

@Document("errors")
data class LogError(
    val key: String,
    val message: String,
    val stackTrace: String,
    val path: String
) : LogEntity()

@Document("changes")
data class LogChange(
    val entityId: String,
    val old: String,
    val new: String,
    val path: String
) : LogEntity()

