package com.cn.langujet.application.arch.log.mongo.services

import com.cn.langujet.application.arch.models.entity.LogEntity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.MongoOperations

sealed class LogService<T : LogEntity> {
    @field:Autowired
    lateinit var mongoOperations: MongoOperations
    
    fun create(entity: T): T {
        entity.id(null)
        return mongoOperations.save(entity)
    }
}
