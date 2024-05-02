package com.cn.langujet.actor.util

import io.jsonwebtoken.Claims
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder

object Auth {
    fun userId(): String {
        return getAuthentication()?.principal?.let {
            if (it is String) it else null
        } ?: ""
    }
    
    fun userEmail(): String {
        return getClaims()?.get("email", String::class.java) ?: ""
    }
    
    fun isAdmin() = hasAuthority("ROLE_ADMIN")
    
    fun isCorrector() = hasAuthority("ROLE_CORRECTOR")
    
    fun isStudent() = hasAuthority("ROLE_STUDENT")
    
    private fun hasAuthority(role: String): Boolean {
        return getAuthentication()?.authorities?.contains(SimpleGrantedAuthority(role)) ?: false
    }
    
    private fun getClaims(): Claims? {
        return getAuthentication()?.credentials?.let {
            if (it is Claims) return it else null
        }
    }
    
    private fun getAuthentication(): Authentication? = SecurityContextHolder.getContext().authentication
}