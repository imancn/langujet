package com.cn.speaktest.application.security.security.repository

import com.cn.speaktest.application.security.security.model.ResetPasswordToken
import com.cn.speaktest.application.security.security.model.User
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.*

interface ResetPasswordTokenRepository : MongoRepository<ResetPasswordToken, String> {

    fun findByUser(user: User): Optional<ResetPasswordToken>
}