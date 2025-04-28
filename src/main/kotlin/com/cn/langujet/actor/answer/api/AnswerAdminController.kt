package com.cn.langujet.actor.answer.api

import com.cn.langujet.application.arch.controller.HistoricalEntityViewController
import com.cn.langujet.domain.answer.AnswerRepository
import com.cn.langujet.domain.answer.AnswerService
import com.cn.langujet.domain.answer.model.AnswerEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/admin/answers")
@Validated
class AnswerAdminController: HistoricalEntityViewController<AnswerRepository, AnswerService, AnswerEntity>()