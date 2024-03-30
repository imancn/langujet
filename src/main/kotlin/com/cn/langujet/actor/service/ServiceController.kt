package com.cn.langujet.actor.service

import com.cn.langujet.actor.service.payload.AvailableExamServicesResponse
import com.cn.langujet.actor.service.payload.ServiceRequest
import com.cn.langujet.domain.service.model.ServiceEntity
import com.cn.langujet.domain.service.service.ServiceService
import jakarta.validation.Valid
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1")
@Validated
class ServiceController(
    private val serviceService: ServiceService
) {
    @PostMapping("/admin/service")
    @PreAuthorize("hasRole('ADMIN')")
    fun createExamService(@Valid @RequestBody request: ServiceRequest): ServiceEntity {
        return serviceService.createService(request)
    }

    @PostMapping("/admin/service/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    fun updateExamService(@PathVariable id: String?, @Valid @RequestBody request: ServiceRequest): ServiceEntity {
        return serviceService.updateService(id!!, request)
    }

    @GetMapping("/admin/service/all")
    @PreAuthorize("hasRole('ADMIN')")
    fun getAllServices(): List<ServiceEntity> {
        return serviceService.getAllServices()
    }

    @GetMapping("/student/service/exam/available")
    fun getAvailableExamServices(): List<AvailableExamServicesResponse> {
        return serviceService.getAvailableExamServices()
    }
}