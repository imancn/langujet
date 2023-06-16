package com.cn.langujet.domain.exam.repository

import com.cn.langujet.domain.exam.model.Suggestion
import org.springframework.data.mongodb.repository.MongoRepository

interface SuggestionRepository : MongoRepository<Suggestion, String>