package com.cn.langujet.domain.exam.service

import com.cn.langujet.domain.exam.model.Exam
import com.cn.langujet.domain.exam.model.ExamVariantEntity
import com.cn.langujet.domain.exam.repository.ExamRepository
import com.cn.langujet.domain.exam.repository.ExamSessionRepository
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.MongoOperations
import org.springframework.data.mongodb.core.aggregation.Aggregation.*
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.stereotype.Service
import kotlin.random.Random

@Service
class ExamGeneratorService(
    private val examRepository: ExamRepository,
    private val examSessionRepository: ExamSessionRepository,
    private val mongoOperations: MongoOperations,
) {
    
    fun getRandomStudentAvailableExam(studentUserId: String, examVariant: ExamVariantEntity): Exam {
        val unavailableExamIds = examSessionRepository.findByStudentUserId(studentUserId).map { it.examId }.distinct()
        val exams = examRepository.findAllByTypeAndActiveAndSectionsNumberAndIdNotIn(
            examVariant.examType,
            true,
            examVariant.sectionTypes.count(),
            unavailableExamIds
        )
        return if (exams.isNotEmpty()) {
            exams.random(Random(System.currentTimeMillis()))
        } else {
            examRepository.findAllByTypeAndActive(examVariant.examType, true).random(Random(System.currentTimeMillis()))
        }
    }
    
    fun countAvailableExamsForVariants(studentUserId: String, examVariantIds: List<String>): Map<String, Int> {
        val examsInSessions = examSessionRepository.findByStudentUserId(studentUserId).distinctBy {
            it.examId
        }.map {
            ObjectId(it.examId)
        }
        
        val aggregation = newAggregation(
            match(Criteria.where("id").`in`(examVariantIds)),
            lookup("exams", "examType", "type", "exams"),
            unwind("exams"),
            match(Criteria.where("exams.active").`is`(true).andOperator(Criteria.where("exams._id").nin(examsInSessions))),
            group("id").count().`as`("availableCount"),
            project("availableCount").and("id").previousOperation()
        )
        
        return mongoOperations.aggregate(aggregation, ExamVariantEntity::class.java, Map::class.java)
            .mappedResults
            .map { it as Map<*, *> }
            .associate { it["id"].toString() to it["availableCount"] as Int }
    }
}