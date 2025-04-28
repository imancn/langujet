package com.cn.langujet.application.arch.mongo

import com.cn.langujet.application.arch.advice.UnprocessableException
import com.cn.langujet.application.arch.log.LoggerService
import com.cn.langujet.application.arch.models.entity.Entity
import com.cn.langujet.application.arch.services.EntityServiceInterface
import com.mongodb.DuplicateKeyException
import com.mongodb.MongoWriteException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.MongoOperations
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Service
import java.lang.reflect.ParameterizedType

@Service
class EntityService<T : Entity<ID>, ID> : EntityServiceInterface<T, ID> {
    @field:Autowired
    lateinit var loggerService: LoggerService
    
    @field:Autowired
    lateinit var mongoOperations: MongoOperations
    
    override fun save(entity: T): T {
        return if (entity.id == null) {
            create(entity, null)
        } else {
            update(entity)
        }
    }
    
    override fun saveMany(entities: List<T>): List<T> {
        return entities.map { save(it) }
    }
    
    override fun create(entity: T, id: ID?): T {
        entity.id(id)
        return try {
            mongoOperations.save(entity)
        } catch (e: Exception) {
            throwUnprocessableException(e)
        }
    }
    
    private fun throwUnprocessableException(e: Exception): Nothing {
        when (e) {
            is MongoWriteException, is DuplicateKeyException -> {
                val errorMessage = e.message ?: "Duplicate key error"
                val collectionMatch = Regex("collection: (\\w+\\.\\w+)").find(errorMessage)
                val keyMatch = Regex("dup key: \\{ ([^}]+) }").find(errorMessage)
                
                val collection = collectionMatch?.groupValues?.get(1) ?: "unknown collection"
                val duplicateKey = keyMatch?.groupValues?.get(1) ?: "unknown fields"
                throw UnprocessableException(
                    "duplicate.key.error", collection, duplicateKey
                )
            }
            else -> throw throw UnprocessableException(
                "duplicate.key.error", "unknown collection", "unknown fields",
            )
        }
    }
    
    override fun createMany(entities: List<T>): List<T> {
        return entities.map { create(it) }
    }
    
    
    override fun update(entity: T): T {
        return doIfExist(entity) {
            mongoOperations.save(entity)
        }
    }
    
    override fun updateMany(entities: List<T>): List<T> {
        return entities.map { update(it) }
    }
    
    override fun getById(id: ID): T {
        val query = Query(Criteria.where("_id").`is`(id))
        return mongoOperations.findOne(query, clazz, collection)
            ?: throw NoSuchElementException("Entity with id $id not found")
    }
    
    override fun tryById(id: ID): T? {
        val query = Query(Criteria.where("_id").`is`(id))
        return mongoOperations.findOne(query, clazz, collection)
    }
    
    override fun findTop(count: Int): List<T> {
        val query = Query().with(PageRequest.of(0, count)).with(Sort.by(Sort.Direction.DESC, "_id"))
        return mongoOperations.find(query, clazz, collection)
    }
    
    override fun find(query: Query, pageable: Pageable?): List<T> {
        return if (pageable == null) {
            return mongoOperations.find(query, clazz, collection)
        } else {
            mongoOperations.find(query.with(pageable).with(pageable.sort), clazz, collection)
        }
    }
    
    override fun find(criteria: Criteria, pageable: Pageable?): List<T> {
        return find(Query(criteria))
    }
    
    @Deprecated("use archive()", ReplaceWith("archive"))
    override fun delete(entity: T): Boolean {
        return doIfExist(entity) { mongoOperations.remove(entity).wasAcknowledged() }
    }
    
    private fun <R> doIfExist(entity: T, operation: () -> R): R {
        val id = entity.id() ?: throw NoSuchElementException("Entity with id ${entity.id()} not found")
        val oldEntity = getById(id)
        loggerService.logChanges(oldEntity, entity)
        return operation.invoke()
    }
    
    @Suppress("UNCHECKED_CAST")
    protected val clazz: Class<T> by lazy {
        (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<T>
    }
    
    protected val collection: String by lazy {
        clazz.getAnnotation(Document::class.java)?.collection
            ?: throw NoSuchElementException("Collection name not found on ${clazz.simpleName}")
    }
}