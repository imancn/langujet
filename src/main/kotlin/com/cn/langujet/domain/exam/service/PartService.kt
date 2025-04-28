package com.cn.langujet.domain.exam.service

import com.cn.langujet.application.arch.mongo.HistoricalEntityService
import com.cn.langujet.domain.exam.model.section.part.PartEntity
import com.cn.langujet.domain.exam.repository.PartRepository
import org.springframework.stereotype.Service

@Service
class PartService(
    override var repository: PartRepository,
) : HistoricalEntityService<PartRepository, PartEntity>()
