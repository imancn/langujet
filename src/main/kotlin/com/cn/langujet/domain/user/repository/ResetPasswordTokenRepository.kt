package com.cn.langujet.domain.user.repository

import com.cn.langujet.application.arch.mongo.HistoricalMongoRepository
import com.cn.langujet.domain.user.model.ResetPasswordTokenEntity
import com.cn.langujet.domain.user.model.UserEntity
import java.util.*

interface ResetPasswordTokenRepository : HistoricalMongoRepository<ResetPasswordTokenEntity> {

    fun findByUser(user: UserEntity): Optional<ResetPasswordTokenEntity>
}