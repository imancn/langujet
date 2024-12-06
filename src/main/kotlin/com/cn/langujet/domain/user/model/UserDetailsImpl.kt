package com.cn.langujet.domain.user.model

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class UserDetailsImpl(
    val id: String,
    val email: String,
    val emailVerified: Boolean,
    @field:JsonIgnore private val password: String?,
    private val authorities: Collection<GrantedAuthority>
) : UserDetails {

    override fun getAuthorities(): Collection<GrantedAuthority> {
        return authorities
    }

    override fun getPassword(): String? {
        return password
    }

    override fun getUsername(): String {
        return id
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val user = other as UserDetailsImpl
        return id == user.id
    }
    
    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + email.hashCode()
        result = 31 * result + emailVerified.hashCode()
        result = 31 * result + password.hashCode()
        result = 31 * result + authorities.sumOf { it.authority.hashCode() }
        return result
    }
    
    companion object {
        fun build(user: UserEntity): UserDetailsImpl {
            val authorities: List<GrantedAuthority> = user.roles.map { role ->
                SimpleGrantedAuthority(
                    role.name
                )
            }
            return UserDetailsImpl(
                user.id!!,
                user.standardEmail,
                user.emailVerified,
                user.password,
                authorities
            )
        }
    }
}