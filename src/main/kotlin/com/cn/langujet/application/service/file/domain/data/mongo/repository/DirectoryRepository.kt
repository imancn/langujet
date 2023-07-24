package com.cn.langujet.application.service.file.domain.data.mongo.repository

import com.cn.langujet.application.service.file.domain.data.mongo.model.Directory
import org.springframework.data.mongodb.repository.MongoRepository

interface DirectoryRepository: MongoRepository<Directory, String>