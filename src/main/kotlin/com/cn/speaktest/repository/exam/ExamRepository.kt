package com.cn.speaktest.repository.exam

import com.cn.speaktest.model.Exam
import org.springframework.data.mongodb.repository.MongoRepository

interface ExamRepository : MongoRepository<Exam, String>