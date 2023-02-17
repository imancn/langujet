package com.cn.speaktest.security.repository

import com.cn.speaktest.security.model.EmailVerificationToken
import com.cn.speaktest.security.model.User
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.Optional

interface EmailVerificationTokenRepository : MongoRepository<EmailVerificationToken, String> {

    fun findByUser(user: User): Optional<EmailVerificationToken>
}