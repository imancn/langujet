package com.cn.langujet.application.service.smtp

import com.cn.langujet.application.advice.InternalServerError
import com.cn.langujet.domain.user.model.EmailVerificationTokenEntity
import com.cn.langujet.domain.user.model.ResetPasswordTokenEntity
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
            throw InternalServerError("Email not sent.\nerror: ${e.message}")
        }
    }

    fun sendWithTemplate(to: String, subject: String, contentParams: Map<String, String>, templateName: String) =
        sendWithTemplate(
            listOf(to),
            subject,
            contentParams,
            templateName
        )

    fun sendEmailVerificationMail(emailVerificationToken: EmailVerificationTokenEntity) {
        val token = emailVerificationToken.token
        val contentParams = mapOf(
            "TOKEN" to token,
        )
        sendWithTemplate(
            emailVerificationToken.user.email, "Verification Mail", contentParams, "email_verification"
        )
    }
    
    fun sendDeleteAccountVerificationMail(emailVerificationToken: EmailVerificationTokenEntity) {
        val token = emailVerificationToken.token
        val contentParams = mapOf(
            "TOKEN" to token,
        )
        sendWithTemplate(
            emailVerificationToken.user.email, "Delete Account Verification Mail", contentParams, "delete_account_verification_mail"
        )
    }

    fun sendResetPasswordMail(resetPasswordToken: ResetPasswordTokenEntity) {
        val token = resetPasswordToken.token
        val contentParams = mapOf(
            "TOKEN" to token,
        )
        sendWithTemplate(
            resetPasswordToken.user.email, "Reset Password Mail", contentParams, "reset_password"
        )
    }
    
    fun sendExamCorrectionNotificationEmail(
        to: String, username: String, examName: String,
        correctionLink: String = "https://app.langujet.com/exams/participated"
    ) {
        val contentParams = mapOf(
            "USERNAME" to (username),
            "EXAM_NAME" to examName,
            "CORRECTION_LINK" to correctionLink,
            "CURRENT_YEAR" to LocalDateTime.now().year.toString()
        )
        sendWithTemplate(
            to,
            "Exam Correction Notification",
            contentParams,
            "exam_correction_notification_mail"
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