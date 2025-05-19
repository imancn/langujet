package com.cn.langujet.application.arch.mongo

import com.cn.langujet.application.arch.advice.UnprocessableException
import com.cn.langujet.application.arch.log.LoggerService
import com.cn.langujet.application.arch.models.entity.Entity
import com.cn.langujet.application.arch.recyclebin.RecycleBin
import com.cn.langujet.application.arch.services.EntityServiceInterface
import com.mongodb.MongoWriteException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DuplicateKeyException
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.FindAndReplaceOptions
import org.springframework.data.mongodb.core.MongoOperations
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Service
import java.lang.reflect.ParameterizedType

@Service
abstract class EntityService<R : MongoRepository<T, ID>, T : Entity<ID>, ID : Any> : EntityServiceInterface<T, ID> {
    @Autowired
    lateinit var recycleBin: RecycleBin

    @field:Autowired
    lateinit var loggerService: LoggerService
    
    @field:Autowired
    lateinit var mongoOperations: MongoOperations
    
    protected abstract var repository: R
    
    override fun save(entity: T): T {
        return repository.save(entity)
    }
    
    override fun saveMany(entities: List<T>): List<T> {
        return entities.map { save(it) }
    }
    
    override fun create(entity: T, id: ID?): T {
        entity.id(id)
        return try {
            repository.save(entity)
        } catch (e: Exception) {
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
                else -> throw e
            }
        }
    }
    
    override fun createMany(entities: List<T>): List<T> {
        return entities.map { create(it) }
    }
    
    
    override fun update(entity: T): T {
        val oldEntity = getById(entity.id())
        loggerService.logChanges(oldEntity, entity)
        val query = Query(Criteria.where("_id").`is`(entity.id()))
        mongoOperations.findAndReplace(query, entity, FindAndReplaceOptions.options().upsert(), clazz, collection)
        return entity
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
        return repository.findAll(PageRequest.of(0, count, Sort.by(Sort.Order.desc("_id")))).content
    }
    
    override fun find(query: Query, pageable: Pageable?): List<T> {
        return if (pageable == null) {
            return mongoOperations.find(
                query,
                clazz, collection)
        } else {
            mongoOperations.find(query.with(pageable).with(pageable.sort), clazz, collection)
        }
    }
    
    override fun find(criteria: Criteria, pageable: Pageable?): List<T> {
        return find(Query(criteria))
    }

    override fun delete(id: ID): Boolean {
        val entity = getById(id)
        recycleBin.drop(entity)
        return mongoOperations.remove(entity).wasAcknowledged()
    }
    
    @Suppress("UNCHECKED_CAST")
    protected val clazz: Class<T> by lazy {
        (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[1] as Class<T>
    }
    
    protected val collection: String by lazy {
        clazz.getAnnotation(Document::class.java)?.collection
            ?: throw NoSuchElementException("Collection name not found on ${clazz.simpleName}")
    }
}