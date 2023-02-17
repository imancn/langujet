package com.cn.speaktest.security.api

interface AuthService {

    fun getUserIdFromAuthorizationHeader(authorizationHeader: String?): String
}