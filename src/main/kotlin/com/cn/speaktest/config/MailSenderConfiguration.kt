package com.cn.speaktest.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.JavaMailSenderImpl
import java.util.*

@Configuration
class MailSenderConfiguration {

    @Bean
    fun mailSender(): JavaMailSender {
        val mailSender = JavaMailSenderImpl()

        mailSender.host = "smtp.gmail.com"
        mailSender.port = 587
        mailSender.username = "langujet@gmail.com"
        mailSender.password = "ehhhzfvdnceabntu"
        val props: Properties = mailSender.javaMailProperties
        props["mail.smtp.starttls.enable"] = "true"
        props["mail.smtp.auth"] = "true"
        props["mail.smtp.connectiontimeout"] = 40000000
        props["mail.smtp.timeout"] = 400000000
        props["mail.smtp.transport.protocol"] = "smtp"
        props["mail.smtp.localhost"] = "127.0.0.1"
        props["mail.debug"] = "true"
        return mailSender
    }
}