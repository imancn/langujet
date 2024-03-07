package com.cn.langujet.domain.security.repository

import com.cn.langujet.domain.security.model.EmailVerificationToken
import com.cn.langujet.application.security.security.model.User
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.*

interface EmailVerificationTokenRepository : MongoRepository<EmailVerificationToken, String> {

    fun findByUser(user: User): Optional<EmailVerificationToken>
}