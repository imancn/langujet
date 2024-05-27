package com.cn.langujet.domain.service.service

import com.cn.langujet.actor.service.payload.ExamVariantResponse.Companion.toExamTypeResponse
import com.cn.langujet.actor.service.payload.GetAvailableExamServicesRequest
import com.cn.langujet.actor.service.payload.GetAvailableExamServicesResponse
import com.cn.langujet.actor.service.payload.ServiceRequest
import com.cn.langujet.actor.util.Auth
import com.cn.langujet.actor.util.models.CustomPage
import com.cn.langujet.actor.util.models.paginate
import com.cn.langujet.application.advice.NotFoundException
import com.cn.langujet.domain.exam.service.ExamGeneratorService
import com.cn.langujet.domain.exam.service.ExamVariantService
import com.cn.langujet.domain.service.model.ServiceEntity
import com.cn.langujet.domain.service.model.ServiceType
import com.cn.langujet.domain.service.repository.ServiceRepository
import org.springframework.stereotype.Service
import java.util.stream.Collectors

@Service
class ServiceService(
    private val serviceRepository: ServiceRepository,
    private val examVariantService: ExamVariantService,
    private val examGeneratorService: ExamGeneratorService
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
    
    fun getAvailableExamServices(request: GetAvailableExamServicesRequest): CustomPage<GetAvailableExamServicesResponse> {
        val examServices = serviceRepository.findByTypeAndActiveOrderByOrder(ServiceType.EXAM, true)
            .filterIsInstance<ServiceEntity.ExamServiceEntity>()
        val examVariants = examVariantService.getExamVariantByIds(examServices.map { it.examVariantId })
        val availableServiceCount = examGeneratorService.countAvailableExamsForVariants(
            Auth.userId(), examVariants.mapNotNull { it.id }
        )
        val response = examServices.stream().map { examService ->
            val examVariant = examVariants.find { it.id == examService.examVariantId }
            GetAvailableExamServicesResponse(
                examService.id ?: "N/A",
                examService.name,
                examService.price,
                examService.discount,
                examVariant?.toExamTypeResponse(),
                availableServiceCount[examVariant?.id ?: ""] ?: 0
            )
        }.filter { service ->
            request.examType?.contains(service.examVariant?.examType) ?: true &&
            request.correctorType?.contains(service.examVariant?.correctorType) ?: true &&
            request.hasDiscount?.let { if (it) service.discount > 0.0 else service.discount == 0.0 } ?: true
        }.collect(Collectors.toList())
        return response.paginate(request.pageSize, request.pageNumber)
    }
    
    fun getByIds(serviceId: String): ServiceEntity {
        return serviceRepository.findById(serviceId)
            .orElseThrow { NotFoundException("Service with id $serviceId not found") }
    }
}