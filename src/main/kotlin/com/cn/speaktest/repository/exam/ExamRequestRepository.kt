package com.cn.speaktest.repository.exam

import com.cn.speaktest.model.ExamRequest
import org.springframework.data.mongodb.repository.MongoRepository

interface ExamRequestRepository : MongoRepository<ExamRequest, String>