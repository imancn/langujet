package com.cn.langujet.domain.user.repository

import com.cn.langujet.application.arch.mongo.HistoricalMongoRepository
import com.cn.langujet.domain.user.model.EmailVerificationTokenEntity
import com.cn.langujet.domain.user.model.UserEntity
import java.util.*

interface EmailVerificationTokenRepository : HistoricalMongoRepository<EmailVerificationTokenEntity> {

    fun findByUser(user: UserEntity): Optional<EmailVerificationTokenEntity>
}