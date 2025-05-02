package com.cn.langujet.application.service.cms.enums

import com.cn.langujet.application.arch.mongo.HistoricalEntityService
import org.springframework.stereotype.Service

@Service
class EnumService(
    override var repository: EnumRepository
) : HistoricalEntityService<EnumRepository, EnumEntity>()