package com.cn.langujet.domain.exam.repository

import com.cn.langujet.domain.exam.model.ExamSession
import com.cn.langujet.domain.exam.model.ExamSessionState
import org.springframework.data.domain.PageRequest
import org.springframework.data.mongodb.repository.MongoRepository

interface ExamSessionRepository : MongoRepository<ExamSession, String> {
    fun findAllByStudentId(studentId: String, pageRequest: PageRequest): List<ExamSession>
    fun findAllByStudentIdAndState(studentId: String, state: ExamSessionState, pageRequest: PageRequest): List<ExamSession>
    fun findAllByProfessorId(professorId: String): List<ExamSession>
    fun existsByStudentIdAndStateContaining(studentId: String, states: List<ExamSessionState>): Boolean
}