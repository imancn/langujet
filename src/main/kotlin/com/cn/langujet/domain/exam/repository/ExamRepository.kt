package com.cn.langujet.domain.exam.repository

import com.cn.langujet.domain.exam.model.ExamEntity
import com.cn.langujet.domain.exam.model.ExamMode
import com.cn.langujet.domain.exam.model.ExamType
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.mongodb.repository.MongoRepository

interface ExamRepository : MongoRepository<ExamEntity, String> {
    fun findAllByNameContainingIgnoreCaseOrderByNameAsc(name: String, pageRequest: PageRequest): Page<ExamEntity>
    fun findAllByTypeAndModeAndActive(type: ExamType, mode: ExamMode, active: Boolean): List<ExamEntity>
    fun findAllByTypeAndModeAndActiveAndIdNotIn(
        type: ExamType, mode: ExamMode, active: Boolean, id: List<String>
    ): List<ExamEntity>
}