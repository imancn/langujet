package com.cn.langujet.domain.student.repository

import com.cn.langujet.domain.student.model.StudentEntity
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.*

interface StudentRepository : MongoRepository<StudentEntity, Long> {
    fun findByUser_Id(userId: Long): Optional<StudentEntity>
}