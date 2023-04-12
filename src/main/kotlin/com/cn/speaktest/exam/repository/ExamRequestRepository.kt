package com.cn.speaktest.exam.repository

import com.cn.speaktest.exam.api.request.ExamRequest
import com.cn.speaktest.student.model.Student
import org.springframework.data.mongodb.repository.MongoRepository

interface ExamRequestRepository : MongoRepository<ExamRequest, String> {
    fun findByExamId(examId: String): List<ExamRequest>
    fun findByStudentId(studentId: String): List<ExamRequest>
    fun existsByStudentAndExamId(student: Student, examId: String): Boolean

}