package com.cn.langujet.application.service.smtp

import com.cn.langujet.application.advice.EmailNotSentException
import com.cn.langujet.domain.user.model.EmailVerificationToken
import com.cn.langujet.domain.user.model.ResetPasswordToken
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
    private val hostRoot = "https://api.langujet.com"
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
            throw EmailNotSentException("Email not sent.\nerror: ${e.message}")
        }
    }

    fun sendWithTemplate(to: String, subject: String, contentParams: Map<String, String>, templateName: String) =
        sendWithTemplate(
            listOf(to),
            subject,
            contentParams,
            templateName
        )

    fun sendEmailVerificationMail(emailVerificationToken: EmailVerificationToken) {
        val email = emailVerificationToken.user.email
        val token = emailVerificationToken.token
        val contentParams = mapOf(
            "TOKEN" to token,
            "LINK" to "$hostRoot/api/v1/auth/signup/email/verify/$email/$token",
        )
        sendWithTemplate(
            email, "Verification Mail", contentParams, "email_verification"
        )
    }
    
    fun sendDeleteAccountVerificationMail(emailVerificationToken: EmailVerificationToken) {
        val email = emailVerificationToken.user.email
        val token = emailVerificationToken.token
        val contentParams = mapOf(
            "TOKEN" to token,
        )
        sendWithTemplate(
            email, "Delete Account Verification Mail", contentParams, "delete_account_verification_mail"
        )
    }

    fun sendResetPasswordMail(resetPasswordToken: ResetPasswordToken) {
        val email = resetPasswordToken.user.email
        val token = resetPasswordToken.token
        val contentParams = mapOf(
            "TOKEN" to token,
            "SITE" to hostRoot,
        )
        sendWithTemplate(
            email, "Reset Password Mail", contentParams, "reset_password"
        )
    }

    fun sendMimeMessage(htmlText: String, message: MimeMessage) {
        val htmlPart = MimeBodyPart()
        htmlPart.setContent(htmlText, "text/html; charset=utf-8")

        val mp = MimeMultipart("alternative")
        mp.addBodyPart(htmlPart)

        message.setContent(mp)
        Transport.send(message)
    }
}