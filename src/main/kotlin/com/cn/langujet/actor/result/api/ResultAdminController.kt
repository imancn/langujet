package com.cn.langujet.actor.result.api

import com.cn.langujet.application.arch.controller.HistoricalEntityViewController
import com.cn.langujet.domain.result.model.ResultEntity
import com.cn.langujet.domain.result.repository.ResultRepository
import com.cn.langujet.domain.result.service.ResultService
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/v1/admin/results")
@Validated
class ResultAdminController : HistoricalEntityViewController<ResultRepository, ResultService, ResultEntity>()