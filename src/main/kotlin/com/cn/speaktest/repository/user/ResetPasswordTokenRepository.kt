package com.cn.speaktest.repository.user

import com.cn.speaktest.model.ResetPasswordToken
import com.cn.speaktest.model.security.User
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.*

interface ResetPasswordTokenRepository : MongoRepository<ResetPasswordToken, String> {

    fun findByUser(user: User): Optional<ResetPasswordToken>
}