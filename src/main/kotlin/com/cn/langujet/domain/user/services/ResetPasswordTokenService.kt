package com.cn.langujet.domain.user.services

import com.cn.langujet.application.arch.mongo.HistoricalEntityService
import com.cn.langujet.domain.user.model.ResetPasswordTokenEntity
import org.springframework.stereotype.Service

@Service
class ResetPasswordTokenService : HistoricalEntityService<ResetPasswordTokenEntity>()
