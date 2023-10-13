package com.cn.langujet.domain.exam.repository

import com.cn.langujet.domain.exam.model.ExamSession
import org.springframework.data.mongodb.repository.MongoRepository

interface ExamSessionRepository : MongoRepository<ExamSession, String> {
    fun findAllByStudentId(studentId: String): List<ExamSession>
    fun findAllByProfessorId(professorId: String): List<ExamSession>
}