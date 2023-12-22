package com.cn.langujet.domain.service.service

import com.cn.langujet.actor.service.payload.AvailableExamServicesResponse
import com.cn.langujet.actor.service.payload.ExamVariantResponse.Companion.toExamTypeResponse
import com.cn.langujet.actor.service.payload.ServiceRequest
import com.cn.langujet.domain.exam.service.ExamVariantService
import com.cn.langujet.domain.service.model.ServiceEntity
import com.cn.langujet.domain.service.model.ServiceType
import com.cn.langujet.domain.service.repository.ServiceRepository
import org.springframework.stereotype.Service

import org.springframework.transaction.annotation.Transactional

@Service
class ServiceService(
    private val serviceRepository: ServiceRepository,
    private val examVariantService: ExamVariantService
) {

    fun createService(request: ServiceRequest): ServiceEntity {
        val serviceEntity = request.convertToServiceEntity<ServiceEntity>()
        return serviceRepository.save(serviceEntity)
    }

    fun updateService(id: String, request: ServiceRequest): ServiceEntity {
        val existingService = serviceRepository.findById(id)
            .orElseThrow { NoSuchElementException("Service with ID: $id not found") }

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

    fun getAvailableExamServices(): List<AvailableExamServicesResponse> {
        return serviceRepository.findByTypeAndActiveOrderByOrder(ServiceType.EXAM, true).map {
            val examService = it as ServiceEntity.ExamServiceEntity
            AvailableExamServicesResponse(
                examService.id ?: "N/A",
                examService.name,
                examService.price,
                examService.discount,
                examVariantService.getExamVariantById(examService.examVariantId).toExamTypeResponse()
            )
        }
    }
}