package com.cn.langujet.domain.service.service

import com.cn.langujet.actor.service.payload.GetAvailableExamServicesRequest
import com.cn.langujet.actor.service.payload.GetAvailableExamServicesResponse
import com.cn.langujet.actor.service.payload.ServiceRequest
import com.cn.langujet.actor.util.Auth
import com.cn.langujet.application.arch.controller.payload.response.PageResponse
import com.cn.langujet.application.arch.controller.payload.response.paginate
import com.cn.langujet.application.arch.advice.UnprocessableException
import com.cn.langujet.domain.exam.service.ExamGeneratorService
import com.cn.langujet.domain.service.model.ServiceEntity
import com.cn.langujet.domain.service.model.ServiceType
import com.cn.langujet.domain.service.repository.ServiceRepository
import org.springframework.stereotype.Service
import java.util.stream.Collectors

@Service
class ServiceService(
    private val serviceRepository: ServiceRepository,
    private val examGeneratorService: ExamGeneratorService,
) {
    fun createService(request: ServiceRequest): ServiceEntity {
        val serviceEntity = request.convertToServiceEntity<ServiceEntity>()
        return serviceRepository.save(serviceEntity)
    }
    
    fun updateService(id: String, request: ServiceRequest): ServiceEntity {
        val existingService =
            serviceRepository.findById(id).orElseThrow { NoSuchElementException("Service with ID: $id not found") }
        
        existingService.apply {
            name = request.name ?: name
            price = request.price ?: price
            discount = request.discount ?: discount
            order = request.order ?: order
            active = request.active ?: active
        }
        
        return serviceRepository.save(existingService)
    }
    
    fun getAllServices(): List<ServiceEntity> {
        return serviceRepository.findAll()
    }
    
    fun getAvailableExamServices(request: GetAvailableExamServicesRequest): PageResponse<GetAvailableExamServicesResponse> {
        val examServices = serviceRepository.findByTypeAndActiveOrderByOrder(ServiceType.EXAM, true)
            .filterIsInstance<ServiceEntity.ExamServiceEntity>()
        val availableServiceCount = examGeneratorService.countAvailableExamsForExamServices(
            Auth.userId(), examServices
        )
        val response = examServices.stream().map { examService ->
            GetAvailableExamServicesResponse(
                examService.id ?: "N/A",
                examService.name,
                examService.price,
                examService.discount,
                examService.examType,
                examService.examMode,
                examService.correctorType,
                availableServiceCount[examService?.id ?: ""] ?: 0
            )
        }.filter { service ->
            request.examType?.contains(service?.examType) ?: true && request.correctorType?.contains(service?.correctorType) ?: true && request.hasDiscount?.let { if (it) service.discount > 0.0 else service.discount == 0.0 } ?: true
        }.collect(Collectors.toList())
        return response.paginate(request.pageSize, request.pageNumber)
    }
    
    fun getById(serviceId: String): ServiceEntity {
        return serviceRepository.findById(serviceId)
            .orElseThrow { UnprocessableException("Service with id $serviceId not found") }
    }
}