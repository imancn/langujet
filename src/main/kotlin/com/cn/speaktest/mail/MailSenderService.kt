package com.cn.speaktest.mail

import com.cn.speaktest.security.model.EmailVerificationToken
import com.cn.speaktest.security.model.ResetPasswordToken
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

    fun sendEmailVerificationMail(emailVerificationToken: EmailVerificationToken) {
        val hostRoot = "http://localhost:8080"
        val email = emailVerificationToken.user.email
        val token = emailVerificationToken.token
        val logoPath = "https://res.cloudinary.com/practicaldev/image/fetch/s--FSZb8Vto--/c_imagga_scale,f_auto,fl_progressive,h_420,q_auto,w_1000/https://dev-to-uploads.s3.amazonaws.com/uploads/articles/x7qr5ksfk3zzmkcabvdm.png"
        val contentParams = mapOf(
            "TOKEN" to token,
            "LINK" to "$hostRoot/api/auth/signup/email/verify/$email/$token",
            "SITE" to hostRoot,
            "LOGO_PATH" to logoPath
        )
        sendWithTemplate(
            email, "Verification Mail", contentParams, "email_verification"
        )
    }

    fun sendResetPasswordMail(resetPasswordToken: ResetPasswordToken) {
        val hostRoot = "http://localhost:8080"
        val email = resetPasswordToken.user.email
        val token = resetPasswordToken.token
        val logoPath = "https://res.cloudinary.com/practicaldev/image/fetch/s--FSZb8Vto--/c_imagga_scale,f_auto,fl_progressive,h_420,q_auto,w_1000/https://dev-to-uploads.s3.amazonaws.com/uploads/articles/x7qr5ksfk3zzmkcabvdm.png"
        val contentParams = mapOf(
            "TOKEN" to token,
            "SITE" to hostRoot,
            "LOGO_PATH" to logoPath
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