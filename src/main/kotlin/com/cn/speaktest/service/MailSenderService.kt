package com.cn.speaktest.service

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import javax.mail.Message
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeBodyPart
import javax.mail.internet.MimeMessage
import javax.mail.internet.MimeMultipart

@Service
class MailSenderService(
    private val session: Session

) {
    private val logger = LoggerFactory.getLogger(javaClass.simpleName)
    fun sendWithTemplate(to: List<String>, subject: String, contentParams: Map<String, String>, templateName: String) {
        val content = JinjaEngineUtil.render("${templateName}.html", contentParams)
        try {
            val message = MimeMessage(session)
            message.setFrom(InternetAddress("langujet@gmail.com"))
            message.setRecipients(Message.RecipientType.TO, to.map { InternetAddress(it) }.toTypedArray())
            message.subject = subject

            sendMimeMessage(content, message)
        } catch (e: Exception) {
            logger.error("Email not sent. error: ${e.message}")
        }
    }

    fun sendWithTemplate(to: String, subject: String, contentParams: Map<String, String>, templateName: String) =
        sendWithTemplate(
            listOf(to),
            subject,
            contentParams,
            templateName
        )

    private fun sendMimeMessage(htmlText: String, message: MimeMessage) {
        val htmlPart = MimeBodyPart()
        htmlPart.setContent(htmlText, "text/html; charset=utf-8")

        val mp = MimeMultipart("alternative")
        mp.addBodyPart(htmlPart)

        message.setContent(mp)
        Transport.send(message)
    }
}