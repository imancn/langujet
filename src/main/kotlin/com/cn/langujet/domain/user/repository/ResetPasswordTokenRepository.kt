package com.cn.langujet.domain.user.repository

import com.cn.langujet.domain.user.model.ResetPasswordToken
import com.cn.langujet.domain.user.model.User
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.*

interface ResetPasswordTokenRepository : MongoRepository<ResetPasswordToken, String> {

    fun findByUser(user: User): Optional<ResetPasswordToken>
}