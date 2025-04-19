package com.cn.langujet.actor.exam.api

import com.cn.langujet.application.arch.controller.HistoricalEntityCrudController
import com.cn.langujet.domain.exam.model.section.part.questions.QuestionEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/admin/questions")
class QuestionAdminController : HistoricalEntityCrudController<QuestionEntity>()