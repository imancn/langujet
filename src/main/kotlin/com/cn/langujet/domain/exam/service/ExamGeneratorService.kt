package com.cn.langujet.domain.exam.service

import com.cn.langujet.domain.exam.model.ExamEntity
import com.cn.langujet.domain.exam.model.enums.ExamMode
import com.cn.langujet.domain.exam.model.enums.ExamType
import com.cn.langujet.domain.exam.repository.ExamRepository
import com.cn.langujet.domain.exam.repository.ExamSessionRepository
import com.cn.langujet.domain.service.model.ServiceEntity
import org.springframework.data.mongodb.core.MongoOperations
import org.springframework.data.mongodb.core.aggregation.Aggregation
import org.springframework.data.mongodb.core.aggregation.Aggregation.*
import org.springframework.data.mongodb.core.aggregation.GroupOperation
import org.springframework.data.mongodb.core.aggregation.MatchOperation
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.stereotype.Service
import kotlin.random.Random

@Service
class ExamGeneratorService(
    private val examRepository: ExamRepository,
    private val examSessionRepository: ExamSessionRepository,
    private val mongoOperations: MongoOperations,
) {
    
    fun getRandomStudentAvailableExam(studentUserId: Long, examService: ServiceEntity.ExamServiceEntity): ExamEntity {
        val unavailableExamIds = examSessionRepository.findByStudentUserId(studentUserId).map { it.examId }.distinct()
        val exams = examRepository.findAllByTypeAndModeAndActive(
            examService.examType,
            examService.examMode,
            true
        ).filter { !unavailableExamIds.contains(it.id) }
        return if (exams.isNotEmpty()) {
            exams.random(Random(System.currentTimeMillis()))
        } else {
            examRepository.findAllByTypeAndModeAndActive(
                examService.examType, examService.examMode, true
            ).random(Random(System.currentTimeMillis()))
        }
    }
    
    fun countAvailableExamsForExamServices(
        studentUserId: Long,
        examServices: List<ServiceEntity.ExamServiceEntity>
    ): Map<Long?, Int> {
        val examsInSessions = examSessionRepository.findByStudentUserId(studentUserId).map { it.examId }.distinct()
        
        val matchCriteria = mutableListOf<Criteria>()
        matchCriteria.add(Criteria.where("type").`in`(examServices.map { it.examType }))
        matchCriteria.add(Criteria.where("mode").`in`(examServices.map { it.examMode }))
        matchCriteria.add(Criteria.where("active").`is`(true))
        matchCriteria.add(Criteria.where("_id").nin(examsInSessions.map { id -> id }))
        
        val matchOperation: MatchOperation = match(Criteria().andOperator(*matchCriteria.toTypedArray()))
        
        val groupOperation: GroupOperation = group("type", "mode").count().`as`("count")
        
        val projectionOperation: ProjectionOperation = project()
            .and("_id.type").`as`("type")
            .and("_id.mode").`as`("mode")
            .and("count").`as`("count")
            .andExclude("_id")
        
        val aggregation: Aggregation = newAggregation(matchOperation, groupOperation, projectionOperation)
        
        val examsCount = mongoOperations.aggregate(
            aggregation, "exams", TypeModeCount::class.java
        ).mappedResults
 
        return examServices.associate { examService ->
            val count = examsCount.find {
                it.type == examService.examType && it.mode == examService.examMode
            }?.count ?: 0
            examService.id to count
        }
    }
    
    data class TypeModeCount(
        val type: ExamType,
        val mode: ExamMode,
        val count: Int
    )
}