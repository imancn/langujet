package com.cn.langujet.actor.correction.api

import com.cn.langujet.application.arch.controller.HistoricalEntityCrudController
import com.cn.langujet.domain.correction.model.CorrectAnswerEntity
import com.cn.langujet.domain.correction.service.CorrectAnswerService
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/admin/correct-answers")
@Validated
class CorrectAnswerController(
    override var service: CorrectAnswerService
) : HistoricalEntityCrudController<CorrectAnswerService, CorrectAnswerEntity>()