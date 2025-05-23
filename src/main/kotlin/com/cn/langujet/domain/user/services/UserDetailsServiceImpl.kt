package com.cn.langujet.domain.user.services

import com.cn.langujet.application.arch.advice.InvalidCredentialException
import com.cn.langujet.domain.user.model.UserDetailsImpl
import com.cn.langujet.domain.user.repository.UserRepository
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
    override fun loadUserByUsername(username: String): UserDetailsImpl {
        return UserDetailsImpl(
            userRepository.findByUsernameAndDeleted(username)
                .orElseThrow { InvalidCredentialException("User Not Found") }
        )
    }
    
    fun userExist(userId: Long): Boolean {
        return userRepository.existsByIdAndDeleted(userId)
    }
}