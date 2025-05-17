package com.cn.langujet.actor.service

import com.cn.langujet.application.arch.controller.HistoricalEntityCrudController
import com.cn.langujet.domain.service.model.ServiceEntity
import com.cn.langujet.domain.service.service.ServiceService
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/admin/services")
@Validated
class ServiceAdminController(
    override var service: ServiceService
) : HistoricalEntityCrudController<ServiceService, ServiceEntity>()