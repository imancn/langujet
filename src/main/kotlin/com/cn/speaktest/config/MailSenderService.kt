package com.cn.speaktest.config

import org.springframework.context.annotation.Configuration
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.JavaMailSenderImpl
import org.springframework.stereotype.Service
import java.util.*
import javax.annotation.PostConstruct

@Configuration
@Service
class MailSenderService {
    private val mailSender = JavaMailSenderImpl()

    @PostConstruct
    private fun configMailSender(): JavaMailSender {
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

    fun sendMail(to: String, subject: String, content: String){
        val message = SimpleMailMessage()
        message.setFrom("langujet@gmail.com")
        message.setTo(to)
        message.setSubject(subject)
        message.setText(content)
        mailSender.send(message)
    }
}