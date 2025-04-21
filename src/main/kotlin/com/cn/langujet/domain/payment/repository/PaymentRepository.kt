package com.cn.langujet.domain.payment.repository

import com.cn.langujet.domain.payment.model.PaymentEntity
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface PaymentRepository : MongoRepository<PaymentEntity, Long>