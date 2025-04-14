package com.cn.langujet.application.arch.controller

import com.cn.langujet.application.arch.mongo.models.SequentialEntity
import com.cn.langujet.application.arch.mongo.sequesnce.SequentialEntityService
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam

abstract class SequentialEntityViewController<T : SequentialEntity> {
    
    abstract val entityService: SequentialEntityService<T>
    
    @GetMapping("/{id}")
    open fun getEntity(@PathVariable id: Long): ResponseEntity<T> {
        val entity = entityService.getById(id)
        return ResponseEntity.ok(entity)
    }
    
    @GetMapping
    open fun getEntities(@RequestParam("criteria") criteria: String): ResponseEntity<List<T>> {
        val queryCriteria = Criteria.where("field").`is`(criteria)
        val entities = entityService.find(queryCriteria)
        return ResponseEntity.ok(entities)
    }
}
