package com.cn.langujet.application.arch.controller

import com.cn.langujet.application.arch.models.entity.HistoricalEntity
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

abstract class HistoricalEntityCrudController<T : HistoricalEntity>: HistoricalEntityViewController<T>() {
    
    @PostMapping @PreAuthorize("hasRole('ADMIN')")
    open fun createEntity(@RequestBody entity: T): ResponseEntity<T> {
        val createdEntity = service.create(entity)
        return ResponseEntity.ok(createdEntity)
    }
    
    @PutMapping @PreAuthorize("hasRole('ADMIN')")
    open fun updateEntity(@RequestBody entity: T): ResponseEntity<T> {
        val updatedEntity = service.update(entity)
        return ResponseEntity.ok(updatedEntity)
    }
    
    @DeleteMapping("/{id}") @PreAuthorize("hasRole('ADMIN')")
    open fun deleteEntity(@PathVariable id: Long): ResponseEntity<Void> {
        val entity = service.getById(id)
        val deleted = service.archive(entity)
        return if (deleted) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }
}
