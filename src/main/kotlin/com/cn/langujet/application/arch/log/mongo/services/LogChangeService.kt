package com.cn.langujet.application.arch.log.mongo.services

import com.cn.langujet.application.arch.log.mongo.models.LogChange
import org.springframework.stereotype.Service

@Service
class LogChangeService : LogService<LogChange>()