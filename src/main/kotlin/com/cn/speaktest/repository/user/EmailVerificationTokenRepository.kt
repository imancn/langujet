package com.cn.speaktest.repository.user

import com.cn.speaktest.model.EmailVerificationToken
import com.cn.speaktest.model.security.User
import org.springframework.data.mongodb.repository.MongoRepository

interface EmailVerificationTokenRepository : MongoRepository<EmailVerificationToken, String> {

    fun findByUser(email: User): EmailVerificationToken?
}