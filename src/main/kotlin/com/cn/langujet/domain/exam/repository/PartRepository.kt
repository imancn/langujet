package com.cn.langujet.domain.exam.repository

import com.cn.langujet.application.arch.mongo.HistoricalMongoRepository
import com.cn.langujet.domain.exam.model.section.part.PartEntity

interface PartRepository : HistoricalMongoRepository<PartEntity>
