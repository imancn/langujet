package com.cn.langujet.application.arch.mongo.sequesnce

import com.cn.langujet.application.arch.mongo.models.SequentialEntity
import com.cn.langujet.application.arch.mongo.service.EntityService
import org.springframework.dao.OptimisticLockingFailureException
import org.springframework.data.mongodb.core.FindAndModifyOptions
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update

abstract class SequentialEntityService<T : SequentialEntity> : EntityService<Long, T>() {
    override fun generateSequence(): Long {
        var retryCount = 0
        val maxRetries = 5
        
        while (retryCount < maxRetries) {
            try {
                val query = Query(Criteria.where("_id").`is`(collection))
                val update = Update().inc("seq", 1)
                val options = FindAndModifyOptions.options().returnNew(true).upsert(true)
                val counter = mongoOperations.findAndModify(query, update, options, IdSequenceEntity::class.java)
                
                return counter?.seq ?: 1
            } catch (ex: OptimisticLockingFailureException) {
                retryCount++
                if (retryCount == maxRetries) {
                    throw RuntimeException("Failed to generate sequence after $maxRetries retries", ex)
                }
            }
        }
        throw RuntimeException("Unexpected error generating sequence")
    }
}