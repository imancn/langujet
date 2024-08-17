package com.cn.langujet.domain.user.services

import com.cn.langujet.application.advice.UnprocessableException
import com.cn.langujet.domain.user.model.UserEntity
import com.cn.langujet.domain.user.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(
    private val repository: UserRepository
) {
    fun getUserByEmail(email: String): UserEntity {
        return repository.findByEmailAndDeleted(
            email.toStandardMailAddress()
        ).orElseThrow { UnprocessableException("User Not Found") }
    }
}

fun String.toStandardMailAddress(): String {
    if (this.contains("..")) throw UnprocessableException("Invalid Email")
    return this.lowercase().let {
        if (it.contains("gmail")) {
            it.replace(".", "").replace("@gmailcom", "@gmail.com")
        } else it
    }
}