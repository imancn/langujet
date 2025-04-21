package com.cn.langujet.application.service.cms.enums

import com.cn.langujet.application.arch.controller.HistoricalEntityCrudController
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/admin/enums")
class EnumController: HistoricalEntityCrudController<EnumEntity>() {
}