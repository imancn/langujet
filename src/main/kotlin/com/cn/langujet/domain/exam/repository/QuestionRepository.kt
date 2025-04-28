package com.cn.langujet.domain.exam.repository

import com.cn.langujet.application.arch.mongo.HistoricalMongoRepository
import com.cn.langujet.domain.exam.model.section.part.questions.QuestionEntity

interface QuestionRepository : HistoricalMongoRepository<QuestionEntity>
