package com.cn.langujet.actor.section.api

import com.cn.langujet.application.arch.controller.HistoricalEntityCrudController
import com.cn.langujet.domain.exam.model.section.SectionEntity
import com.cn.langujet.domain.exam.service.SectionService
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/v1/admin/sections")
class SectionController(override val service: SectionService) : HistoricalEntityCrudController<SectionService, SectionEntity>(service)