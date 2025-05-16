package com.cn.langujet.application.arch.controller

import com.cn.langujet.application.arch.BundleService
import com.cn.langujet.application.arch.controller.payload.response.MessageResponse
import com.cn.langujet.application.arch.models.entity.HistoricalEntity
import com.cn.langujet.application.arch.mongo.HistoricalEntityService
import com.cn.langujet.application.arch.mongo.HistoricalMongoRepository
import io.swagger.v3.oas.annotations.Operation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

abstract class HistoricalEntityCrudController<R : HistoricalMongoRepository<E>, S : HistoricalEntityService<R, E>, E : HistoricalEntity> :
    HistoricalEntityViewController<R, S, E>() {
    
    override lateinit var service: S
    
    @Autowired
    lateinit var bundle: BundleService
    
    @Operation(
        summary = "Create a new entity",
        description = "Creates a new entity. Requires ADMIN role."
    )
    @PostMapping("/create") @PreAuthorize("hasRole('ADMIN')")
    open fun create(@RequestBody dto: E): ResponseEntity<E> {
        return ResponseEntity.ok(service.create(dto))
    }
    
    @Operation(
        summary = "Update an existing entity",
        description = "Updates an existing entity. Requires ADMIN role."
    )
    @PostMapping("/update") @PreAuthorize("hasRole('ADMIN')")
    open fun update(@RequestBody dto: E): ResponseEntity<E> {
        return ResponseEntity.ok(service.update(dto))
    }
    
    @Operation(
        summary = "Archive an entity",
        description = "Archives an entity by ID. Requires ADMIN role."
    )
    @PostMapping("delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    open fun delete(@PathVariable id: Long): ResponseEntity<MessageResponse> {
        return ResponseEntity.ok(
            bundle.getMessageResponse(
                if (service.delete(id)) "successful" else "failed"
            )
        )
    }
}
