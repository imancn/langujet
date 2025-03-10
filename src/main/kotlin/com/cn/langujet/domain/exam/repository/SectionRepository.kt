package com.cn.langujet.domain.exam.repository

import com.cn.langujet.domain.exam.model.section.SectionEntity
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface SectionRepository : MongoRepository<SectionEntity, String> {
    fun findAllByExamId(examId: String): List<SectionEntity>
    fun findByExamIdAndOrder(examId: String, order: Int): Optional<SectionEntity>
    fun existsByExamIdAndOrder(examId: String, order: Int): Boolean
}