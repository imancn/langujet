package com.cn.langujet.domain.exam.repository

import com.cn.langujet.domain.exam.model.ExamTypeEntity
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface ExamTypeRepository: MongoRepository<ExamTypeEntity, String> {
    fun findAllByActiveOrderByOrder(isActive: Boolean): List<ExamTypeEntity>
}
