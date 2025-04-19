package com.cn.langujet.application.arch.log.mongo.services

import com.cn.langujet.application.arch.log.mongo.models.LogError
import org.springframework.stereotype.Service

@Service
class LogErrorService : LogService<LogError>()