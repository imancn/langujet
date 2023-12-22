package com.cn.langujet.domain.service.repository

import com.cn.langujet.domain.service.model.ServiceEntity
import com.cn.langujet.domain.service.model.ServiceType
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface ServiceRepository: MongoRepository<ServiceEntity, String> {
    fun findByTypeAndActiveOrderByOrder(type: ServiceType, active: Boolean): List<ServiceEntity>
}