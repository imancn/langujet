package com.cn.speaktest.exam.repository

import com.cn.speaktest.exam.model.Suggestion
import org.springframework.data.mongodb.repository.MongoRepository

interface SuggestionRepository : MongoRepository<Suggestion, String>