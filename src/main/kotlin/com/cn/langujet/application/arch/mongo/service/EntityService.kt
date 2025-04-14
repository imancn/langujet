package com.cn.langujet.application.arch.mongo.service

import com.cn.langujet.application.arch.log.LoggerService
import com.cn.langujet.application.arch.mongo.models.HistoricalEntity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.MongoOperations
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import java.lang.reflect.ParameterizedType

abstract class EntityService<ID, T : HistoricalEntity<ID>> {
    
    @Autowired
    protected lateinit var loggerService: LoggerService
    
    @Autowired
    protected lateinit var mongoOperations: MongoOperations
    
    fun create(entity: T): T {
        entity.id = generateSequence()
        return mongoOperations.save(entity)
    }
    
    fun createMany(entities: List<T>): List<T> {
        return entities.map { create(it) }
//        return mongoOperations.save(
//            entities.onEach { entity ->
//                entity.id = generateSequence()
//            }
//        )
    }
    
    fun getById(id: ID): T {
        val query = Query(Criteria.where("_id").`is`(id))
        return mongoOperations.findOne(query, clazz, collection)
            ?: throw NoSuchElementException("Entity with id $id not found")
    }
    
    fun tryById(id: ID): T? {
        val query = Query(Criteria.where("_id").`is`(id))
        return mongoOperations.findOne(query, clazz, collection)
    }
    
    fun find(criteria: Criteria): List<T> {
        val query = Query(criteria)
        return mongoOperations.find(query, clazz, collection)
    }
    
    fun find(criteria: Criteria, page: Int, size: Int): List<T> {
        val pageable: Pageable = PageRequest.of(page, size)
        val query = Query(criteria).with(pageable)
        return mongoOperations.find(query, clazz, collection)
    }
    
    fun find(criteria: Criteria, page: Int, size: Int, sort: Sort): List<T> {
        val pageable: Pageable = PageRequest.of(page, size, sort)
        val query = Query(criteria).with(pageable)
        return mongoOperations.find(query, clazz, collection)
    }
    
    fun find(criteria: Criteria, pageable: Pageable): List<T> {
        val query = Query(criteria).with(pageable)
        return mongoOperations.find(query, clazz, collection)
    }
    
    fun update(entity: T): T {
        return doIfExist(entity) {
            mongoOperations.save(entity)
        }
    }
    
    fun delete(entity: T): Boolean {
        return delete(entity)
    }
    
    @Deprecated("Don't use")
    fun delete(entity: T, hard: Boolean = false): Boolean {
        if (hard) {
            return doIfExist(entity) { mongoOperations.remove(entity).wasAcknowledged() }
        } else {
            update(
                entity.also {
                    it.deleted = true
                }
            )
            return true
        }
    }
    
    private fun <R> doIfExist(entity: T, operation: () -> R): R {
        val id = entity.id ?: throw NoSuchElementException("Entity with id ${entity.id} not found")
        val query = Query(Criteria.where("_id").`is`(id))
        val oldEntity = getById(id)
        loggerService.logChanges(oldEntity, entity)
        return operation.invoke()
    }
    
    protected abstract fun generateSequence(): ID
    
    @Suppress("UNCHECKED_CAST")
    private val clazz: Class<T> by lazy {
        (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<T>
    }
    
    protected val collection: String by lazy {
        clazz.getAnnotation(Document::class.java)?.collection
            ?: throw NoSuchElementException("Collection name not found on ${clazz.simpleName}")
    }
}