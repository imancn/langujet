package com.cn.speaktest.repository.user

import com.cn.speaktest.model.EmailVerificationToken
import com.cn.speaktest.model.security.User
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.Optional

interface EmailVerificationTokenRepository : MongoRepository<EmailVerificationToken, String> {

    fun findByUser(user: User): Optional<EmailVerificationToken>
}