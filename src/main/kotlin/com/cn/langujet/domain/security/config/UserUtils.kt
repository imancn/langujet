package com.cn.langujet.domain.security.config

import com.cn.langujet.domain.security.model.UserDetailsImpl
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails

object UserUtils {
    fun getCurrentUserId(): String? {
        val userDetails = getCurrentUserDetails() as? UserDetailsImpl
        return userDetails?.id
    }
    
    fun getCurrentUserEmail(): String? {
        val userDetails = getCurrentUserDetails() as? UserDetailsImpl
        return userDetails?.email
    }
    
    fun isAdmin() = hasAuthority("ROLE_ADMIN")
    
    fun isProfessor() = hasAuthority("ROLE_PROFESSOR")
    
    fun isStudent() = hasAuthority("ROLE_STUDENT")
    
    private fun getCurrentUserDetails(): UserDetails? {
        val authentication = SecurityContextHolder.getContext().authentication
        return authentication?.principal as? UserDetails
    }
    
    private fun hasAuthority(role: String): Boolean {
        val userDetails = getCurrentUserDetails() as? UserDetailsImpl
        return userDetails?.authorities?.contains(SimpleGrantedAuthority(role)) ?: false
    }
}