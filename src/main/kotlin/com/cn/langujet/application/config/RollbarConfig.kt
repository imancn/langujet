package com.cn.langujet.application.config

import com.rollbar.notifier.Rollbar
import com.rollbar.notifier.config.ConfigBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RollbarConfig {

    @Value("\${rollbar.access.token}")
    private lateinit var accessToken: String

    @Bean
    fun rollbar(): Rollbar {
        val config = ConfigBuilder.withAccessToken(accessToken)
            .environment("production")
            .build()

        return Rollbar.init(config)
    }
}
