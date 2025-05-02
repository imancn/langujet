package com.cn.langujet.domain.service.service

import com.cn.langujet.actor.service.payload.GetAvailableExamServicesRequest
import com.cn.langujet.actor.service.payload.GetAvailableExamServicesResponse
import com.cn.langujet.actor.service.payload.ServiceRequest
import com.cn.langujet.application.service.users.Auth
import com.cn.langujet.application.arch.advice.UnprocessableException
import com.cn.langujet.application.arch.controller.payload.response.PageResponse
import com.cn.langujet.application.arch.controller.payload.response.paginate
import com.cn.langujet.application.arch.models.entity.Entity
import com.cn.langujet.application.arch.mongo.HistoricalEntityService
import com.cn.langujet.domain.exam.service.ExamGeneratorService
import com.cn.langujet.domain.service.model.ServiceEntity
import com.cn.langujet.domain.service.model.ServiceType
import com.cn.langujet.domain.service.repository.ServiceRepository
import org.springframework.stereotype.Service
import java.util.stream.Collectors

@Service
class ServiceService(
    override var repository: ServiceRepository,
    private val examGeneratorService: ExamGeneratorService,
) : HistoricalEntityService<ServiceRepository, ServiceEntity>() {
    fun createService(request: ServiceRequest): ServiceEntity {
        val serviceEntity = request.convertToServiceEntity<ServiceEntity>()
        return save(serviceEntity)
    }
    
    fun updateService(id: Long, request: ServiceRequest): ServiceEntity {
        val existingService =
            repository.findById(id).orElseThrow { NoSuchElementException("Service with ID: $id not found") }
        
        existingService.apply {
            name = request.name ?: name
            price = request.price ?: price
            discount = request.discount ?: discount
            order = request.order ?: order
            active = request.active ?: active
        }
        
        return save(existingService)
    }
    
    fun getAllServices(): List<ServiceEntity> {
        return repository.findAll()
    }
    
    fun getAvailableExamServices(request: GetAvailableExamServicesRequest): PageResponse<GetAvailableExamServicesResponse> {
        val examServices = repository.findByTypeAndActiveOrderByOrder(ServiceType.EXAM, true)
            .filterIsInstance<ServiceEntity.ExamServiceEntity>()
        val availableServiceCount = examGeneratorService.countAvailableExamsForExamServices(
            Auth.userId(), examServices
        )
        val response = examServices.stream().map { examService ->
            GetAvailableExamServicesResponse(
                examService.id ?: Entity.UNKNOWN_ID,
                examService.name,
                examService.price,
                examService.discount,
                examService.examType,
                examService.examMode,
                examService.correctorType,
                availableServiceCount[examService?.id ?: Entity.UNKNOWN_ID] ?: 0
            )
        }.filter { service ->
            request.examType?.contains(service?.examType) ?: true && request.correctorType?.contains(service?.correctorType) ?: true && request.hasDiscount?.let { if (it) service.discount > 0.0 else service.discount == 0.0 } ?: true
        }.collect(Collectors.toList())
        return response.paginate(request.pageSize, request.pageNumber)
    }
    
    override fun getById(id: Long): ServiceEntity {
        return repository.findById(id)
            .orElseThrow { UnprocessableException("Service with id $id not found") }
    }
}