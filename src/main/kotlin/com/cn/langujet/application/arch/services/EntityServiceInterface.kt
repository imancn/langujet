package com.cn.langujet.application.arch.services

import com.cn.langujet.application.arch.models.entity.Entity
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query

interface EntityServiceInterface<T : Entity<ID>, ID> {
    fun save(entity: T): T
    fun saveMany(entities: List<T>): List<T>
    
    fun create(entity: T, id: ID? = null): T
    fun createMany(entities: List<T>): List<T>
    
    fun update(entity: T): T
    fun updateMany(entities: List<T>): Any
    
    fun getById(id: ID): T
    fun tryById(id: ID): T?
    
    fun findTop(count: Int): List<T>
    fun find(query: Query, pageable: Pageable? = null): List<T>
    fun find(criteria: Criteria, pageable: Pageable? = null): List<T>
    
    fun delete(entity: T): Boolean
}