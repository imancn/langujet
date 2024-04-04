package com.cn.langujet.domain.user.repository

import com.cn.langujet.domain.user.model.EmailVerificationToken
import com.cn.langujet.domain.user.model.User
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.*

interface EmailVerificationTokenRepository : MongoRepository<EmailVerificationToken, String> {

    fun findByUser(user: User): Optional<EmailVerificationToken>
}