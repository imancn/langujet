package com.cn.speaktest.application.smtp

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.*
import javax.mail.PasswordAuthentication
import javax.mail.Session

@Configuration
class MailSenderConfiguration {

    @Bean
    fun session(): Session {
        val props = Properties()
        props["mail.smtp.auth"] = "true"
        props["mail.smtp.starttls.enable"] = "true"
        props["mail.smtp.connectiontimeout"] = 4000
        props["mail.smtp.timeout"] = 4000
        props["mail.smtp.transport.protocol"] = "smtp"
        props["mail.smtp.localhost"] = "127.0.0.1"
        props["mail.smtp.host"] = "smtp.gmail.com"
        props["mail.smtp.port"] = 587
        props["mail.smtp.ssl.protocols"] = "TLSv1.2"

        return Session.getInstance(props, object : javax.mail.Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication("langujet@gmail.com", "ehhhzfvdnceabntu")
            }
        })
    }
}