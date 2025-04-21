package com.cn.langujet.domain.exam.repository

import com.cn.langujet.domain.exam.model.section.SectionEntity
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface SectionRepository : MongoRepository<SectionEntity, Long> {
    fun findAllByExamId(examId: Long): List<SectionEntity>
    fun findByExamIdAndOrder(examId: Long, order: Int): Optional<SectionEntity>
    fun existsByExamIdAndOrder(examId: Long, order: Int): Boolean
}