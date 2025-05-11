package com.cn.langujet.application.service.smtp

import com.cn.langujet.application.arch.advice.InternalServerError
import com.cn.langujet.application.service.otp.OTP
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.LocalDateTime
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

    fun sendOTP(otp: OTP, email: String) {
        val token = otp.token
        val contentParams = mapOf(
            "TOKEN" to token,
        )
        val template = otp.key.template ?: throw InternalServerError("mail.sender.send.otp.template.not.found")
        sendWithTemplate(
            otp.key.subject, contentParams, template, email
        )
    }

    fun sendExamCorrectionNotificationEmail(
        email: String, username: String, examName: String,
        correctionLink: String = "https://app.langujet.com/exams/participated"
    ) {
        val contentParams = mapOf(
            "USERNAME" to (username),
            "EXAM_NAME" to examName,
            "CORRECTION_LINK" to correctionLink,
            "CURRENT_YEAR" to LocalDateTime.now().year.toString()
        )
        sendWithTemplate(
            "Exam Correction Notification",
            contentParams,
            "exam_correction_notification_mail",
            email
        )
    }

    private fun sendWithTemplate(
        subject: String,
        contentParams: Map<String, String>,
        templateName: String,
        vararg receivers: String,
    ) {
        val content = JinjaEngineUtil.render("${templateName}.html", contentParams)
        try {
            val message = MimeMessage(session)
            message.setFrom(InternetAddress("langujet@gmail.com"))
            message.setRecipients(Message.RecipientType.TO, receivers.map { InternetAddress(it) }.toTypedArray())
            message.subject = subject
            val htmlPart = MimeBodyPart()
            htmlPart.setContent(content, "text/html; charset=utf-8")

            val mp = MimeMultipart("alternative")
            mp.addBodyPart(htmlPart)

            message.setContent(mp)
            Transport.send(message)
        } catch (e: Exception) {
            logger.error("Email not sent. error: ${e.message}")
            throw InternalServerError("Email not sent.\nerror: ${e.message}")
        }
    }
}