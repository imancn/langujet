package com.cn.langujet.application.arch.log.mongo.services

import com.cn.langujet.application.arch.log.mongo.models.LogHttpError
import org.springframework.stereotype.Service

@Service
class LogHttpErrorService : LogService<LogHttpError>()