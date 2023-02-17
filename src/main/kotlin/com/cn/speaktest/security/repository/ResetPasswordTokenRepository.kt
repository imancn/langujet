package com.cn.speaktest.security.repository

import com.cn.speaktest.security.model.ResetPasswordToken
import com.cn.speaktest.security.model.User
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.*

interface ResetPasswordTokenRepository : MongoRepository<ResetPasswordToken, String> {

    fun findByUser(user: User): Optional<ResetPasswordToken>
}