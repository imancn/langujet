package com.cn.langujet.domain.exam.repository

import com.cn.langujet.domain.exam.model.ExamVariantEntity
import org.springframework.data.mongodb.repository.MongoRepository

interface ExamVariantRepository: MongoRepository<ExamVariantEntity, String>{
    fun findAllByIdIn(ids: List<String>): List<ExamVariantEntity>
}
