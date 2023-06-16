package com.cn.langujet.domain.exam.repository

import com.cn.langujet.domain.exam.model.ExamRequest
import com.cn.langujet.domain.student.model.Student
import org.springframework.data.mongodb.repository.MongoRepository

interface ExamRequestRepository : MongoRepository<ExamRequest, String> {
    fun findByExamId(examId: String): List<ExamRequest>
    fun findByStudentId(studentId: String): List<ExamRequest>
    fun existsByStudentAndExamId(student: Student, examId: String): Boolean

}