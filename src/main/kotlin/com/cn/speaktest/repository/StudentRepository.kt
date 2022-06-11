package com.cn.speaktest.repository

import com.cn.speaktest.model.Student
import com.cn.speaktest.model.security.User
import org.springframework.data.mongodb.repository.MongoRepository

interface StudentRepository : MongoRepository<Student, String> {
    fun findByUser(user: User): Student?
}