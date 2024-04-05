package com.cn.langujet.domain.exam.repository

import com.cn.langujet.actor.exam.payload.ExamDTO
import com.cn.langujet.actor.exam.payload.ExamSessionResponse
import com.cn.langujet.actor.exam.payload.ExamSessionSearchRequest
import com.cn.langujet.domain.exam.model.Exam
import com.cn.langujet.domain.exam.model.ExamSession
import com.cn.langujet.domain.exam.model.ExamVariantEntity
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
    fun searchExamSessions(searchRequest: ExamSessionSearchRequest, userId: String): List<ExamSessionResponse> {
        val foundExamIdsByName = if (!searchRequest.examName.isNullOrEmpty()) {
            mongoOperations.find(
                Query(Criteria.where("name").regex(".*" + searchRequest.examName + ".*", "i")), Exam::class.java
            ).map { it.id ?: "" }
        } else null
        
        val criteria = Criteria.where("studentUserId").`is`(userId)
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
        
        return sessions.mapNotNull { session ->
            val exam = exams[session.examId]
            val examVariant = examVariants[session.examVariantId]
            val correctionTypeCriteria = searchRequest.correctionTypes.isNullOrEmpty() || examVariant?.correctionType in searchRequest.correctionTypes
            val examTypeCriteria = searchRequest.examTypes.isNullOrEmpty() || (examVariant?.examType in searchRequest.examTypes)
            if (examTypeCriteria && correctionTypeCriteria) {
                ExamSessionResponse(session, exam?.let { ExamDTO(it) }, examVariant?.correctionType)
            } else null
        }
    }
}