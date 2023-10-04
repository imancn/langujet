package com.cn.langujet.domain.exam.repository

import com.cn.langujet.domain.exam.model.ExamRequest
import com.cn.langujet.domain.exam.model.ExamType
import com.cn.langujet.domain.exam.model.SectionType
import org.springframework.data.mongodb.repository.MongoRepository

interface ExamRequestRepository : MongoRepository<ExamRequest, String> {
    fun findByStudentId(studentId: String): List<ExamRequest>
    fun existsByStudentIdAndExamTypeAndSectionType(
        studentId: String,
        examType: ExamType,
        sectionType: SectionType?
    ): Boolean

}