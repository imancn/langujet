package com.cn.langujet.domain.user.services

import com.cn.langujet.domain.user.model.User

interface AuthService {

    fun getUserIdFromAuthorizationHeader(authorizationHeader: String?): String
    fun getUserById(userId: String): User
    fun getUserByAuthToken(auth: String?): User
    fun doesUserOwnsAuthToken(authToken: String, id: String?): Boolean
}