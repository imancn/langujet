package com.cn.speaktest.exam.repository

import com.cn.speaktest.exam.model.Answer
import org.springframework.data.mongodb.repository.MongoRepository

interface AnswerRepository : MongoRepository<Answer, String>