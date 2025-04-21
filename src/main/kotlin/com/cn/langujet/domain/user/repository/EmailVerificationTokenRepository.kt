package com.cn.langujet.domain.user.repository

import com.cn.langujet.domain.user.model.EmailVerificationTokenEntity
import com.cn.langujet.domain.user.model.UserEntity
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.*

interface EmailVerificationTokenRepository : MongoRepository<EmailVerificationTokenEntity, Long> {

    fun findByUser(user: UserEntity): Optional<EmailVerificationTokenEntity>
}