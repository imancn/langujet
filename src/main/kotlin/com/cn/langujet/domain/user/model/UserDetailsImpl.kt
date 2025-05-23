package com.cn.langujet.domain.user.model

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User

class UserDetailsImpl(
    username: String,
    @field:JsonIgnore
    private val password: String?,
    private val authorities: Collection<GrantedAuthority>,
    @field:JsonIgnore
    val id: Long,
    val email: String,
    @field:JsonIgnore
    val emailVerified: Boolean
) : User(
    username, password, authorities
) {
    constructor(user: UserEntity): this(
        id = user.id!!,
        username = user.username,
        email = user.email,
        emailVerified = user.emailVerified,
        password = user.password,
        authorities = user.roles.map { role ->
            SimpleGrantedAuthority(
                role.name
            )
        }
    )

    override fun getAuthorities(): Collection<GrantedAuthority> {
        return authorities
    }

    override fun getPassword(): String? {
        return password
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
}