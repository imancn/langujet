package com.cn.langujet.domain.exam.repository

import com.cn.langujet.actor.exam.payload.ExamSessionSearchResponse
import com.cn.langujet.actor.exam.payload.ExamSessionSearchStudentRequest
import com.cn.langujet.actor.util.models.CustomPage
import com.cn.langujet.domain.exam.model.ExamEntity
import com.cn.langujet.domain.exam.model.ExamSessionEntity
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
    fun searchExamSessions(
        searchRequest: ExamSessionSearchStudentRequest,
        userId: String
    ): CustomPage<ExamSessionSearchResponse> {
        var foundExams = if (!searchRequest.examName.isNullOrEmpty()) {
            mongoOperations.find(
                Query(Criteria.where("name").regex(".*" + searchRequest.examName + ".*", "i")), ExamEntity::class.java
            )
        } else null
        
        val criteria = Criteria.where("studentUserId").`is`(userId)
        val sessionCriteria = criteria.apply {
            foundExams?.let { exams -> and("examId").`in`(exams.mapNotNull { it?.id }) }
            searchRequest.examTypes?.let { examTypes -> and("examType").`in`(examTypes) }
            searchRequest.correctorTypes?.let { correctorTypes -> and("correctorType").`in`(correctorTypes) }
            searchRequest.states?.let { and("state").`in`(it) }
            searchRequest.startDateInterval?.let { and("startDate").gte(Date(it.start)).lte(Date(it.end)) }
            searchRequest.correctionDateInterval?.let { and("correctionDate").gte(Date(it.start)).lte(Date(it.end)) }
        }
        
        val sessions = mongoOperations.find(
            Query(sessionCriteria).with(Sort.by(Sort.Direction.DESC, "startDate"))
                .with(PageRequest.of(searchRequest.pageNumber, searchRequest.pageSize)), ExamSessionEntity::class.java
        )
        
        val totalSessionCount = mongoOperations.count(Query(sessionCriteria), ExamSessionEntity::class.java)
        
        if (foundExams.isNullOrEmpty()) {
            foundExams = mongoOperations.find(
                Query(Criteria.where("id").`in`(sessions.map { it.examId }.distinct())), ExamEntity::class.java
            )
        }
        
        val sessionResponses = sessions.map { session ->
            val exam = foundExams.find { it.id == session.examId }
            ExamSessionSearchResponse(session, exam)
        }
        return CustomPage(
            sessionResponses, searchRequest.pageSize, searchRequest.pageNumber, totalSessionCount
        )
    }
}