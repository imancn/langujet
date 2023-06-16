package com.cn.langujet.domain.security.services

import com.cn.langujet.application.security.security.model.User

interface AuthService {

    fun getUserIdFromAuthorizationHeader(authorizationHeader: String?): String
    fun getUserById(userId: String): User
    fun getUserByAuthToken(auth: String?): User
    fun doesUserOwnsAuthToken(authToken: String, id: String?): Boolean
}