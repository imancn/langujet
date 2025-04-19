package com.cn.langujet.application.arch.controller

import com.cn.langujet.application.arch.controller.payload.request.search.SearchRequest
import com.cn.langujet.application.arch.controller.payload.response.PageResponse
import com.cn.langujet.application.arch.models.entity.HistoricalEntity
import com.cn.langujet.application.arch.mongo.HistoricalEntityService
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.query.Query
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam

abstract class HistoricalEntityViewController<T : HistoricalEntity> {
    
    @field:Autowired
    lateinit var service: HistoricalEntityService<T>
    
    @field:Autowired
    lateinit var mapper: ObjectMapper
    
    @GetMapping("/{id}") @PreAuthorize("hasRole('ADMIN')")
    open fun getEntity(@PathVariable id: Long): ResponseEntity<T> {
        val entity = service.getById(id)
        return ResponseEntity.ok(entity)
    }
    
    @GetMapping @PreAuthorize("hasRole('ADMIN')")
    open fun getTop(@RequestParam count: Int = 10): ResponseEntity<List<T>> {
        return ResponseEntity.ok(service.findTop(count))
    }
    
    @PostMapping("/search") @PreAuthorize("hasRole('ADMIN')")
    open fun getEntities(@RequestBody request: SearchRequest?): ResponseEntity<PageResponse<T>> {
        if (request != null) {
            val pageable = if (!request.sorts.isNullOrEmpty()) {
                val sort = Sort.by(
                    request.sorts.map {
                        if (it.direction) Sort.Order.asc(it.key) else Sort.Order.desc(it.key)
                    }
                )
                PageRequest.of(request.page.number, request.page.size, sort)
            } else {
                PageRequest.of(request.page.number, request.page.size)
            }
            val entities = service.find(request.filters?.query() ?: Query(), pageable)
            return ResponseEntity.ok(PageResponse(entities, request.page))
        } else {
            return ResponseEntity.ok(PageResponse(service.findTop(10), 0, 10, 20))
        }
    }
}
