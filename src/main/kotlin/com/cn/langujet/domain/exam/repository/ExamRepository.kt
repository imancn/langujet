package com.cn.langujet.domain.exam.repository

import com.cn.langujet.application.arch.mongo.HistoricalMongoRepository
import com.cn.langujet.domain.exam.model.ExamEntity
import com.cn.langujet.domain.exam.model.enums.ExamMode
import com.cn.langujet.domain.exam.model.enums.ExamType
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest

interface ExamRepository : HistoricalMongoRepository<ExamEntity> {
    fun findAllByNameContainingIgnoreCaseOrderByNameAsc(name: String, pageRequest: PageRequest): Page<ExamEntity>
    fun findAllByTypeAndModeAndActive(type: ExamType, mode: ExamMode, active: Boolean): List<ExamEntity>
}