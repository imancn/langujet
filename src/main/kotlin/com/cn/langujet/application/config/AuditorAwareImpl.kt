package com.cn.langujet.application.config

import org.springframework.data.domain.AuditorAware
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import java.util.*

@Component
class AuditorAwareImpl : AuditorAware<String> {
    override fun getCurrentAuditor(): Optional<String> {
        val principal = SecurityContextHolder.getContext().authentication?.name
        return Optional.ofNullable(principal)
    }
}
