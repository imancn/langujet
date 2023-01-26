package com.cn.speaktest.service

import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service

@Service
class MailSenderService(
    private val mailSender: JavaMailSender
) {

    fun sendNoReplyMail(to: String, subject: String, content: String){
        val message = SimpleMailMessage()
        message.setFrom("langujet@gmail.com")
        message.setTo(to)
        message.setSubject(subject)
        message.setText(content)
        message.setReplyTo("no-reply@gmail.com")
        mailSender.send(message)
    }
}