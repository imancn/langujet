package com.cn.langujet.domain.exam.repository

import com.cn.langujet.actor.exam.payload.ExamDTO
import com.cn.langujet.actor.exam.payload.ExamSessionResponse
import com.cn.langujet.actor.exam.payload.ExamSessionSearchRequest
import com.cn.langujet.actor.util.models.CustomPage
import com.cn.langujet.domain.exam.model.Exam
import com.cn.langujet.domain.exam.model.ExamSession
import com.cn.langujet.domain.exam.model.ExamVariantEntity
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
    fun searchExamSessions(searchRequest: ExamSessionSearchRequest, userId: String): CustomPage<ExamSessionResponse> {
        var foundExams = if (!searchRequest.examName.isNullOrEmpty()) {
            mongoOperations.find(
                Query(Criteria.where("name").regex(".*" + searchRequest.examName + ".*", "i")), Exam::class.java
            )
        } else null
        
        val examVariantQuery =
            if (!searchRequest.correctorTypes.isNullOrEmpty() && !searchRequest.examTypes.isNullOrEmpty()) {
                Query(
                    Criteria.where("examType").`in`(searchRequest.examTypes).and("correctorType")
                        .`in`(searchRequest.correctorTypes)
                )
            } else if (!searchRequest.examTypes.isNullOrEmpty()) {
                Query(
                    Criteria.where("examType").`in`(searchRequest.examTypes)
                )
            } else if (!searchRequest.correctorTypes.isNullOrEmpty()) {
                Query(
                    Criteria.where("correctorType").`in`(searchRequest.correctorTypes)
                )
            } else null
        
        var foundExamVariants = examVariantQuery?.let { mongoOperations.find(it, ExamVariantEntity::class.java) }
        
        val criteria = Criteria.where("studentUserId").`is`(userId)
        val sessionCriteria = criteria.apply {
            foundExams?.let { exams -> and("examId").`in`(exams.mapNotNull { it?.id }) }
            foundExamVariants?.let { examVariants -> and("examVariantId").`in`(examVariants.mapNotNull { it?.id }) }
            searchRequest.states?.let { and("state").`in`(it) }
            searchRequest.startDateInterval?.let { and("startDate").gte(Date(it.start)).lte(Date(it.end)) }
            searchRequest.correctionDateInterval?.let { and("correctionDate").gte(Date(it.start)).lte(Date(it.end)) }
        }
        
        val sessions = mongoOperations.find(
            Query(sessionCriteria)
                .with(Sort.by(Sort.Direction.DESC, "startDate"))
                .with(PageRequest.of(searchRequest.pageNumber, searchRequest.pageSize)),
            ExamSession::class.java
        )
        
        val totalSessionCount = mongoOperations.count(Query(sessionCriteria), ExamSession::class.java)
        
        if (foundExams.isNullOrEmpty()) {
            foundExams = mongoOperations.find(
                Query(Criteria.where("id").`in`(sessions.map { it.examId }.distinct())), Exam::class.java
            )
        }
        
        if (foundExamVariants.isNullOrEmpty()) {
            foundExamVariants = mongoOperations.find(
                Query(
                    Criteria.where("id").`in`(
                        sessions.map { it.examVariantId }.distinct())
                ),
                ExamVariantEntity::class.java
            )
        }
        val sessionResponses = sessions.map { session ->
            val exam = foundExams.find { it.id == session.examId }
            val examVariant = foundExamVariants.find { it.id == session.examVariantId }
            ExamSessionResponse(session, exam?.let { ExamDTO(it) }, examVariant?.correctorType)
        }
        return CustomPage<ExamSessionResponse>(
            sessionResponses,
            searchRequest.pageSize,
            searchRequest.pageNumber,
            totalSessionCount
        )
    }
}