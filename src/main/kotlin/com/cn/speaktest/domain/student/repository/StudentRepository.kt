package com.cn.speaktest.domain.student.repository

import com.cn.speaktest.domain.student.model.Student
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.*

interface StudentRepository : MongoRepository<Student, String> {
    fun findByUser_Id(userId: String): Optional<Student>
}