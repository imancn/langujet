package com.cn.speaktest.domain.exam.repository

import com.cn.speaktest.domain.exam.model.Suggestion
import org.springframework.data.mongodb.repository.MongoRepository

interface SuggestionRepository : MongoRepository<Suggestion, String>