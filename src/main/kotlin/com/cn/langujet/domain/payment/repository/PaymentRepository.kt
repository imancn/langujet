package com.cn.langujet.domain.payment.repository

import com.cn.langujet.application.arch.mongo.HistoricalMongoRepository
import com.cn.langujet.domain.payment.model.PaymentEntity
import org.springframework.stereotype.Repository

@Repository
interface PaymentRepository : HistoricalMongoRepository<PaymentEntity>