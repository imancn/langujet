package com.cn.speaktest.repository.exam

import com.cn.speaktest.model.ExamRequest
import com.cn.speaktest.model.Student
import org.springframework.data.mongodb.repository.MongoRepository

interface ExamRequestRepository : MongoRepository<ExamRequest, String> {
    fun existsByStudent(student: Student): Boolean

}