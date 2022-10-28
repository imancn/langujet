package com.cn.speaktest.repository.exam

import com.cn.speaktest.model.Suggestion
import org.springframework.data.mongodb.repository.MongoRepository

interface SuggestionRepository : MongoRepository<Suggestion, String>