package com.cn.langujet.application.arch.controller

import com.cn.langujet.application.arch.controller.payload.request.search.SearchRequest
import com.cn.langujet.application.arch.controller.payload.response.PageResponse
import com.cn.langujet.application.arch.models.entity.HistoricalEntity
import com.cn.langujet.application.arch.mongo.HistoricalEntityService
import com.cn.langujet.application.arch.mongo.HistoricalMongoRepository
import io.swagger.v3.oas.annotations.Operation
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.query.Query
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

abstract class HistoricalEntityViewController<R : HistoricalMongoRepository<E>, S : HistoricalEntityService<R, E>, E : HistoricalEntity> {
    protected open lateinit var service: S
    
    @Operation(
        summary = "Get an entity by id",
        description = "Gets an entity by id. Requires ADMIN role."
    )
    @GetMapping("/{id}") @PreAuthorize("hasRole('ADMIN')")
    open fun getEntity(@PathVariable id: Long): ResponseEntity<E> {
        val entity = service.getById(id)
        return ResponseEntity.ok(entity)
    }
    
    @Operation(
        summary = "Get 10 latest created entities",
        description = "Gets 10 latest created entities. Requires ADMIN role."
    )
    @GetMapping @PreAuthorize("hasRole('ADMIN')")
    open fun getTop(@RequestParam count: Int = 10): ResponseEntity<List<E>> {
        return ResponseEntity.ok(service.findTop(count))
    }
    
    @Operation(
        summary = "Search entities with advanced filtering and pagination",
        description = """
        ## Advanced Search API
        
        Performs a paginated search with filtering and sorting capabilities.
        Returns results in a standardized page response format.
        
        ### Features:
        - **Filtering**: Apply multiple filters using logical AND conditions
        - **Sorting**: Sort by multiple fields with ascending/descending direction
        - **Pagination**: Control page size and number
        - **Default behavior**: Returns top 10 entities when no request body provided
        
        ### Request Structure:
        ```json
        {
          "filters": {
            "expressions": [
              {
                "key": "fieldName",
                "values": ["value1", "value2"]
              }
            ]
          },
          "sorts": [
            {
              "key": "fieldName",
              "direction": true // true=ascending, false=descending
            }
          ],
          "page": {
            "number": 0, // page index (0-based)
            "size": 20   // items per page
          }
        }
        ```
        
        ### Filter Behavior:
        - Each expression creates an `IN` clause (field IN [values])
        - Multiple expressions are combined with AND logic
        - Omit filters to disable filtering
        
        ### Sorting:
        - Supports multiple sort fields
        - Direction: `true` = ascending, `false` = descending
    """
    )
    @PostMapping("/search") @PreAuthorize("hasRole('ADMIN')")
    open fun getEntities(
        @RequestBody request: SearchRequest?
    ): ResponseEntity<PageResponse<E>> {
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
