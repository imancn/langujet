package com.cn.langujet.application.service.cms.contents

import com.cn.langujet.application.arch.controller.HistoricalEntityCrudController
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/admin/contents")
class ContentController: HistoricalEntityCrudController<ContentEntity>()