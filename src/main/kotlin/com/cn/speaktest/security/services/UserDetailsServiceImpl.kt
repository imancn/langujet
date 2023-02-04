package com.cn.speaktest.security.services

import com.cn.speaktest.advice.NotFoundException
import com.cn.speaktest.repository.user.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserDetailsServiceImpl(
    private var userRepository: UserRepository
) : UserDetailsService {
    @Transactional
    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findByUsername(username)
            ?: throw NotFoundException("User Not Found. username: $username")
        return UserDetailsImpl.build(user)
    }
}