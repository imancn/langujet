package com.cn.langujet.domain.correction.repository

import com.cn.langujet.domain.correction.model.CorrectAnswer
import org.springframework.data.mongodb.repository.MongoRepository

interface CorrectAnswerRepository : MongoRepository<CorrectAnswer, String>