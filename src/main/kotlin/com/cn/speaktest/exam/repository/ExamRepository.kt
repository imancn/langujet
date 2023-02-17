package com.cn.speaktest.exam.repository

import com.cn.speaktest.exam.model.Exam
import org.springframework.data.mongodb.repository.MongoRepository

interface ExamRepository : MongoRepository<Exam, String>