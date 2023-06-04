package com.cn.speaktest.domain.exam.repository

import com.cn.speaktest.domain.exam.model.ExamInfo
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query

interface ExamRepository : MongoRepository<ExamInfo, String> {

    @Query("{\$or: [{'id': ?0}, {'name': { \$regex: ?1, \$options: 'i' }}, {'sectionsNumber': ?2}, {'questionNumber': ?3}, {'examDuration': ?4}]}")
    fun findAllExamsByFilters(
        id: String?,
        name: String?,
        sectionsNumber: Int?,
        questionNumber: Int?,
        examDuration: Long?,
        pageRequest: PageRequest
    ): Page<ExamInfo>
}