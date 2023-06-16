package com.cn.speaktest.application.thirdparty.smtp

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.*
import javax.mail.PasswordAuthentication
import javax.mail.Session

@Configuration
class MailSenderConfiguration {

    @Value("\${mail.username}")
    private lateinit var username: String

    @Value("\${mail.password}")
    private lateinit var password: String

    @Value("\${mail.smtp.auth}")
    private lateinit var auth: String

    @Value("\${mail.smtp.starttls.enable}")
    private lateinit var starttls: String

    @Value("\${mail.smtp.connectiontimeout}")
    private lateinit var connectionTimeout: String

    @Value("\${mail.smtp.timeout}")
    private lateinit var timeout: String

    @Value("\${mail.smtp.transport.protocol}")
    private lateinit var transportProtocol: String

    @Value("\${mail.smtp.localhost}")
    private lateinit var localhost: String

    @Value("\${mail.smtp.host}")
    private lateinit var host: String

    @Value("\${mail.smtp.port}")
    private lateinit var port: String

    @Value("\${mail.smtp.ssl.protocols}")
    private lateinit var sslProtocols: String

    @Bean
    fun mailSession(): Session {
        val props = Properties()
        props["mail.smtp.auth"] = auth
        props["mail.smtp.starttls.enable"] = starttls
        props["mail.smtp.connectiontimeout"] = connectionTimeout
        props["mail.smtp.timeout"] = timeout
        props["mail.smtp.transport.protocol"] = transportProtocol
        props["mail.smtp.localhost"] = localhost
        props["mail.smtp.host"] = host
        props["mail.smtp.port"] = port
        props["mail.smtp.ssl.protocols"] = sslProtocols

        return Session.getInstance(props, object : javax.mail.Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication(username, password)
            }
        })
    }
}