package com.cn.langujet.domain.exam.repository

import com.cn.langujet.domain.exam.model.Exam
import com.cn.langujet.domain.exam.model.ExamType
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.mongodb.repository.MongoRepository

interface ExamRepository : MongoRepository<Exam, String> {
    fun findAllByNameContainingIgnoreCaseOrderByNameAsc(name: String, pageRequest: PageRequest): Page<Exam>
    fun findAllByTypeAndActive(type: ExamType, active: Boolean): List<Exam>
}