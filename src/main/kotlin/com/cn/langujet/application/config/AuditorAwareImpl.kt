package com.cn.langujet.application.config

import org.springframework.data.domain.AuditorAware
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import java.util.*

@Component
class AuditorAwareImpl : AuditorAware<Long> {
    override fun getCurrentAuditor(): Optional<Long> {
        val principal = SecurityContextHolder.getContext().authentication?.name?.toLong()
        return Optional.ofNullable(principal)
    }
}
