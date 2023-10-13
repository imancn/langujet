package com.cn.langujet.domain.exam.repository

import com.cn.langujet.domain.exam.model.Section
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface SectionRepository : MongoRepository<Section, String> {
    fun findAllByExamId(examId: String): List<Section>
    fun findByExamIdAndOrder(examId: String, order: Int): Optional<Section>
}