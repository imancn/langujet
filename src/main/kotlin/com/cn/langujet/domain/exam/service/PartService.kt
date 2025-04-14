package com.cn.langujet.domain.exam.service

import com.cn.langujet.application.arch.mongo.sequesnce.SequentialEntityService
import com.cn.langujet.domain.exam.model.section.part.PartEntity
import org.springframework.stereotype.Service

@Service
class PartService : SequentialEntityService<PartEntity>()
