package com.cn.langujet.domain.user.services

import com.cn.langujet.application.arch.mongo.HistoricalEntityService
import com.cn.langujet.domain.user.model.EmailVerificationTokenEntity
import com.cn.langujet.domain.user.repository.EmailVerificationTokenRepository
import org.springframework.stereotype.Service

@Service
class EmailVerificationTokenService :
    HistoricalEntityService<EmailVerificationTokenRepository, EmailVerificationTokenEntity>()
