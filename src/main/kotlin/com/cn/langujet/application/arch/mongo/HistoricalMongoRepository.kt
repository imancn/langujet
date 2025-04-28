package com.cn.langujet.application.arch.mongo

import com.cn.langujet.application.arch.models.entity.HistoricalEntity
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface HistoricalMongoRepository<T : HistoricalEntity> : MongoRepository<T, Long>
