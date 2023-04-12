package com.cn.speaktest.security.api

import com.cn.speaktest.security.model.User

interface AuthService {

    fun getUserIdFromAuthorizationHeader(authorizationHeader: String?): String
    fun getUserById(userId: String): User
    fun getUserByAuthToken(auth: String?): User
    fun doesUserOwnsAuthToken(authToken: String, id: String?): Boolean
}