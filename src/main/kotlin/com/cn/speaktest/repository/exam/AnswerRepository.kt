package com.cn.speaktest.repository.exam

import com.cn.speaktest.model.Answer
import org.springframework.data.mongodb.repository.MongoRepository

interface AnswerRepository : MongoRepository<Answer, String>