package com.cn.speaktest.application.security.security.services

import com.cn.speaktest.application.advice.InvalidTokenException
import com.cn.speaktest.application.security.security.model.UserDetailsImpl
import com.cn.speaktest.application.security.security.repository.UserRepository
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
    override fun loadUserByUsername(userId: String): UserDetails {
        return UserDetailsImpl.build(
            userRepository.findById(userId).orElseThrow { InvalidTokenException("User Not Found") }
        )
    }

    fun userExist(userId: String): Boolean {
        return userRepository.existsById(userId)
    }
}