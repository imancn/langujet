package com.cn.langujet.application.arch.log.mongo.services

import com.cn.langujet.application.arch.log.mongo.models.LogInfo
import org.springframework.stereotype.Service

@Service
class LogInfoService : LogService<LogInfo>()