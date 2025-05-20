package com.cn.langujet.application.arch

import com.cn.langujet.application.arch.models.entity.Entity
import io.jsonwebtoken.Claims
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder

object Auth {
    const val UNDEFINED = -1L
    const val INTERNAL_SERVICE = -2L
    const val EXTERNAL_SERVICE = -3L
    const val SCHEDULED = -4L
    
    fun username(): String {
        return getAuthentication()?.principal?.let {
            if (it is String) it else null
        } ?: "undefined"
    }
    
    fun userId(): Long {
        return getAuthentication()?.name?.toLong() ?: Entity.UNKNOWN_ID
    }
    
    fun email(): String {
        return getClaims()?.get("email", String::class.java) ?: ""
    }
    
    fun isAdmin() = hasAuthority("ROLE_ADMIN")
    
    fun isCorrector() = hasAuthority("ROLE_CORRECTOR")
    
    fun isStudent() = hasAuthority("ROLE_STUDENT")
    
    fun claim(key: String): String = getClaims()?.get(key, String::class.java) ?: ""
    
    fun setCustomUserId(userId: Long) {
        SecurityContextHolder.getContext().authentication = UsernamePasswordAuthenticationToken(
            userId, null, null
        )
    }
    
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