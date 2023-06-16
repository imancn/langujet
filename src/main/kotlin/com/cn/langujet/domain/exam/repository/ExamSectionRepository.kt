package com.cn.langujet.domain.exam.repository

import com.cn.langujet.domain.exam.model.ExamSection
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface ExamSectionRepository : MongoRepository<ExamSection, String>