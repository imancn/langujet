package com.cn.langujet.domain.exam.repository

import com.cn.langujet.application.arch.mongo.HistoricalMongoRepository
import com.cn.langujet.domain.exam.model.section.SectionEntity
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface SectionRepository : HistoricalMongoRepository<SectionEntity> {
    fun findAllByExamId(examId: Long): List<SectionEntity>
    fun findByExamIdAndOrder(examId: Long, order: Int): Optional<SectionEntity>
    fun existsByExamIdAndOrder(examId: Long, order: Int): Boolean
}