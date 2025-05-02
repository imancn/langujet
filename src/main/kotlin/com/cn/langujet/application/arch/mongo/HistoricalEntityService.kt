package com.cn.langujet.application.arch.mongo

import com.cn.langujet.application.arch.models.entity.HistoricalEntity
import org.springframework.dao.OptimisticLockingFailureException
import org.springframework.data.mongodb.core.FindAndModifyOptions
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Service

@Service
abstract class HistoricalEntityService<R : HistoricalMongoRepository<T>, T : HistoricalEntity> : EntityService<R, T, Long>() {
    
    abstract override var repository: R
    
    fun create(entity: T): T {
        return super.create(entity, generateSequence())
    }
    
    override fun createMany(entities: List<T>): List<T> {
        return entities.map { create(it) }
    }
    
    fun archive(entity: T): Boolean {
        return update(entity.also { it.deleted = true }).id != null
    }
    
    fun restore(entity: T): Boolean {
        return update(entity.also { it.deleted = false }).id != null
    }
    
    private fun generateSequence(): Long {
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