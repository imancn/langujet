package com.cn.langujet.domain.student.repository

import com.cn.langujet.application.arch.mongo.HistoricalMongoRepository
import com.cn.langujet.domain.student.model.StudentEntity
import java.util.*

interface StudentRepository : HistoricalMongoRepository<StudentEntity> {
    fun findByUser_Id(userId: Long): Optional<StudentEntity>
}