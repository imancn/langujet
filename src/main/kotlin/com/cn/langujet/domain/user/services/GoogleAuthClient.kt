package com.cn.langujet.domain.user.services

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(name = "google-oauth", url = "https://oauth2.googleapis.com")
interface GoogleAuthClient {
    
    @PostMapping("/token")
    fun getGoogleAccessToken(
        @RequestParam("code") authCode: String,
        @RequestParam("client_id") clientId: String,
        @RequestParam("client_secret") clientSecret: String,
        @RequestParam("redirect_uri") redirectUri: String,
        @RequestParam("grant_type") grantType: String = "authorization_code"
    ): GoogleTokenResponse
    
    @GetMapping("/oauth2/v3/userinfo")
    fun getGoogleUserInfo(@RequestParam("access_token") accessToken: String): GoogleUserInfo
}

data class GoogleTokenResponse(
    val accessToken: String,
    val tokenType: String,
    val expiresIn: Long
)

data class GoogleUserInfo(
    val email: String,
    val name: String,
    val picture: String
)
