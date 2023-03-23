package com.cn.speaktest.security.api

import com.cn.speaktest.security.model.User

interface AuthService {

    fun getUserIdFromAuthorizationHeader(authorizationHeader: String?): String
    fun getUserByAuthToken(auth: String?): User
}