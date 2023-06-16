package com.cn.speaktest.application.thirdparty.file.domain.data.mongo.repository

import com.cn.speaktest.application.thirdparty.file.domain.data.mongo.model.Directory
import org.springframework.data.mongodb.repository.MongoRepository

interface DirectoryRepository: MongoRepository<Directory, String>