package com.cn.langujet.domain.service.repository

import com.cn.langujet.application.arch.mongo.HistoricalMongoRepository
import com.cn.langujet.domain.service.model.ServiceEntity
import com.cn.langujet.domain.service.model.ServiceType
import org.springframework.stereotype.Repository

@Repository
interface ServiceRepository : HistoricalMongoRepository<ServiceEntity> {
    fun findByTypeAndActiveOrderByOrder(type: ServiceType, active: Boolean): List<ServiceEntity>
}