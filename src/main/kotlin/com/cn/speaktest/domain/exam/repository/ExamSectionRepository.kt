package com.cn.speaktest.domain.exam.repository

import com.cn.speaktest.domain.exam.model.ExamSection
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface ExamSectionRepository : MongoRepository<ExamSection, String>