package com.cn.speaktest.application.security.security.repository

import com.cn.speaktest.application.security.security.model.EmailVerificationToken
import com.cn.speaktest.application.security.security.model.User
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.*

interface EmailVerificationTokenRepository : MongoRepository<EmailVerificationToken, String> {

    fun findByUser(user: User): Optional<EmailVerificationToken>
}