package com.cn.langujet.domain.exam.repository

import com.cn.langujet.domain.exam.model.Exam
import com.cn.langujet.domain.exam.model.ExamMode
import com.cn.langujet.domain.exam.model.ExamType
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.mongodb.repository.MongoRepository

interface ExamRepository : MongoRepository<Exam, String> {
    fun findAllByNameContainingIgnoreCaseOrderByNameAsc(name: String, pageRequest: PageRequest): Page<Exam>
    fun findAllByTypeAndModeAndActive(type: ExamType, mode: ExamMode, active: Boolean): List<Exam>
    fun findAllByTypeAndModeAndActiveAndIdNotIn(
        type: ExamType, mode: ExamMode, active: Boolean, id: List<String>
    ): List<Exam>
}