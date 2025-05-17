package com.cn.langujet.actor.exam.api

import com.cn.langujet.application.arch.controller.HistoricalEntityCrudController
import com.cn.langujet.domain.exam.model.section.part.PartEntity
import com.cn.langujet.domain.exam.service.PartService
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/admin/parts")
class PartAdminController(
    override var service: PartService
) : HistoricalEntityCrudController<PartService, PartEntity>()