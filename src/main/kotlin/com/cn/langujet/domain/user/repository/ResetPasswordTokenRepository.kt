package com.cn.langujet.domain.user.repository

import com.cn.langujet.domain.user.model.ResetPasswordTokenEntity
import com.cn.langujet.domain.user.model.UserEntity
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.*

interface ResetPasswordTokenRepository : MongoRepository<ResetPasswordTokenEntity, String> {

    fun findByUser(user: UserEntity): Optional<ResetPasswordTokenEntity>
}