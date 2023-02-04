package com.cn.speaktest.repository.user

import com.cn.speaktest.model.Student
import com.cn.speaktest.model.security.User
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.Optional

interface StudentRepository : MongoRepository<Student, String> {
    fun findByUser(user: User): Optional<Student>
}