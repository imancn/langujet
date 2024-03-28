package com.cn.langujet.domain.exam.repository

import com.cn.langujet.actor.exam.payload.ExamDTO
import com.cn.langujet.actor.exam.payload.ExamSessionResponse
import com.cn.langujet.actor.exam.payload.ExamSessionSearchRequest
import com.cn.langujet.domain.exam.model.Exam
import com.cn.langujet.domain.exam.model.ExamSession
import com.cn.langujet.domain.exam.model.ExamVariantEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.MongoOperations
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Component
import java.util.*

@Component
class ExamSessionCustomRepository(
    private val mongoOperations: MongoOperations
) {
    fun searchExamSessions(searchRequest: ExamSessionSearchRequest, userId: String): Page<ExamSessionResponse> {
        val foundExamIdsByName = if (!searchRequest.examName.isNullOrEmpty()) {
            mongoOperations.find(
                Query(Criteria.where("name").regex(".*" + searchRequest.examName + ".*", "i")), Exam::class.java
            ).map { it.id ?: "" }
        } else null
        
        val criteria = Criteria.where("studentId").`is`(userId)
        val sessionCriteria = criteria.apply {
            foundExamIdsByName?.let { and("examId").`in`(foundExamIdsByName) }
            searchRequest.states?.let { and("state").`in`(it) }
            searchRequest.startDateInterval?.let { and("startDate").gte(Date(it.start)).lte(Date(it.end)) }
            searchRequest.correctionDateInterval?.let { and("correctionDate").gte(Date(it.start)).lte(Date(it.end)) }
        }
        
        val sessions = mongoOperations.find(
            Query(sessionCriteria).with(Sort.by(Sort.Direction.DESC, "startDate")), ExamSession::class.java
        )
        
        val examIds = sessions.map { it.examId }.distinct()
        val exams = mongoOperations.find(
            Query(Criteria.where("id").`in`(examIds)), Exam::class.java
        ).associateBy { it.id }
        
        val examVariantIds = sessions.map { it.examVariantId }.distinct()
        val examVariants = mongoOperations.find(
            Query(Criteria.where("id").`in`(examVariantIds)), ExamVariantEntity::class.java
        ).associateBy { it.id }
        
        val examSessions = sessions.mapNotNull { session ->
            val exam = exams[session.examId]
            val examVariant = examVariants[session.examVariantId]
            val correctionTypeCriteria = searchRequest.correctionTypes.isNullOrEmpty() || examVariant?.correctionType in searchRequest.correctionTypes
            val examTypeCriteria = searchRequest.examTypes.isNullOrEmpty() || (examVariant?.examType in searchRequest.examTypes)
            if (examTypeCriteria && correctionTypeCriteria) {
                ExamSessionResponse(session, exam?.let { ExamDTO(it) }, examVariant?.correctionType)
            } else null
        }
        
        val total = examSessions.size.toLong()
        val pageRequest = PageRequest.of(searchRequest.pageNumber, searchRequest.pageSize)
        return PageImpl(examSessions, pageRequest, total)
    }
}