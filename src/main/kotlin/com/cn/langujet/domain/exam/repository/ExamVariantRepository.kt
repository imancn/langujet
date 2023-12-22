package com.cn.langujet.domain.exam.repository

import com.cn.langujet.domain.exam.model.ExamVariantEntity
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface ExamVariantRepository: MongoRepository<ExamVariantEntity, String>
