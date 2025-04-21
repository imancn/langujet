package com.cn.langujet.domain.user.services

import com.cn.langujet.application.arch.advice.UnprocessableException
import com.cn.langujet.application.arch.mongo.HistoricalEntityService
import com.cn.langujet.domain.user.model.UserEntity
import com.cn.langujet.domain.user.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(
    private val repository: UserRepository
) : HistoricalEntityService<UserEntity>() {
    
    fun getUserByEmail(email: String): UserEntity {
        return repository.findByUsernameAndDeleted(
            email.toStandardMail()
        ).orElseThrow { UnprocessableException("User Not Found") }
    }
}

fun String.toStandardMail(): String {
    if (this.contains("..")) throw UnprocessableException("Invalid Email")
    return this.lowercase().let {
        if (it.contains("gmail")) {
            it.replace(".", "").replace("@gmailcom", "@gmail.com")
        } else it
    }
}