package com.cn.langujet.application.arch.controller

import com.cn.langujet.application.arch.mongo.models.SequentialEntity
import com.cn.langujet.application.arch.mongo.sequesnce.SequentialEntityService
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

abstract class SequentialEntityCrudController<T : SequentialEntity> {
    
    abstract val entityService: SequentialEntityService<T>
    
    @PostMapping
    open fun createEntity(@RequestBody entity: T): ResponseEntity<T> {
        val createdEntity = entityService.create(entity)
        return ResponseEntity.ok(createdEntity)
    }
    
    @GetMapping("/{id}")
    open fun getEntity(@PathVariable id: Long): ResponseEntity<T> {
        val entity = entityService.getById(id)
        return ResponseEntity.ok(entity)
    }
    
    @GetMapping
    open fun getEntities(@RequestParam("criteria") criteria: String): ResponseEntity<List<T>> {
        val queryCriteria = Criteria.where("field").`is`(criteria) // todo: Customize criteria parsing as needed
        val entities = entityService.find(queryCriteria)
        return ResponseEntity.ok(entities)
    }
    
    @PutMapping("/{id}")
    open fun updateEntity(@PathVariable id: Long, @RequestBody entity: T): ResponseEntity<T> {
        val updatedEntity = entityService.update(entity)
        return ResponseEntity.ok(updatedEntity)
    }
    
    @DeleteMapping("/{id}")
    open fun deleteEntity(@PathVariable id: Long, @RequestParam("hard") hard: Boolean): ResponseEntity<Void> {
        val entity = entityService.getById(id)
        val deleted = entityService.delete(entity, hard)
        return if (deleted) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }
}
