package com.cn.langujet.actor.corrector.api

import com.cn.langujet.application.arch.controller.HistoricalEntityViewController
import com.cn.langujet.domain.corrector.CorrectorEntity
import com.cn.langujet.domain.corrector.CorrectorRepository
import com.cn.langujet.domain.corrector.CorrectorService
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/admin/correctors")
@Validated
class CorrectorAdminController :
    HistoricalEntityViewController<CorrectorRepository, CorrectorService, CorrectorEntity>()