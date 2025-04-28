package com.cn.langujet.actor.section.api

import com.cn.langujet.application.arch.controller.HistoricalEntityCrudController
import com.cn.langujet.domain.exam.model.section.SectionEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/v1/admin/sections")
class SectionController : HistoricalEntityCrudController<SectionEntity>()