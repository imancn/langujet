package com.cn.langujet.application.service.file.domain.data.repository

import com.cn.langujet.application.arch.mongo.HistoricalMongoRepository
import com.cn.langujet.application.service.file.domain.data.model.FileEntity

interface FileRepository : HistoricalMongoRepository<FileEntity>