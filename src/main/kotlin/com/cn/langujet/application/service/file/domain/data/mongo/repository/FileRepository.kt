package com.cn.langujet.application.service.file.domain.data.mongo.repository

import com.cn.langujet.application.service.file.domain.data.mongo.model.File
import org.springframework.data.mongodb.repository.MongoRepository

interface FileRepository: MongoRepository<File, String>