package com.cn.langujet.domain.exam.service

import com.cn.langujet.application.arch.mongo.HistoricalEntityService
import com.cn.langujet.domain.exam.model.section.part.questions.QuestionEntity
import org.springframework.stereotype.Service

@Service
class QuestionService : HistoricalEntityService<QuestionEntity>()