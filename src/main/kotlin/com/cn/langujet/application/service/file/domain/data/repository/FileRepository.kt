package com.cn.langujet.application.service.file.domain.data.repository

import com.cn.langujet.application.service.file.domain.data.model.FileEntity
import org.springframework.data.mongodb.repository.MongoRepository

interface FileRepository : MongoRepository<FileEntity, String>