package com.cn.langujet.domain.exam.service

import com.cn.langujet.application.arch.mongo.HistoricalEntityService
import com.cn.langujet.domain.exam.model.section.part.questions.QuestionEntity
import com.cn.langujet.domain.exam.repository.QuestionRepository
import org.springframework.stereotype.Service

@Service
class QuestionService : HistoricalEntityService<QuestionRepository, QuestionEntity>()