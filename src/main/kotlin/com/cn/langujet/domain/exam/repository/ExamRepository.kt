package com.cn.langujet.domain.exam.repository

import com.cn.langujet.domain.exam.model.Exam
import com.cn.langujet.domain.exam.model.ExamType
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query

interface ExamRepository : MongoRepository<Exam, String> {

    @Query("{\$or: [{'id': ?0}, {'name': { \$regex: ?1, \$options: 'i' }}, {'sectionsNumber': ?2}, {'questionNumber': ?3}, {'examDuration': ?4}]}")
    fun findAllExamsByFilters(
        id: String?,
        name: String?,
        sectionsNumber: Int?,
        questionNumber: Int?,
        examDuration: Long?,
        pageRequest: PageRequest
    ): Page<Exam>

    @Query(value = "{ 'examType' : ?0 }", fields = "{ 'id' : 1 }")
    fun findIdsByExamType(examType: ExamType): List<String>
}