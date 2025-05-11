package com.cn.langujet.application.service.otp

import com.cn.langujet.application.arch.models.entity.HistoricalEntity
import org.springframework.data.mongodb.core.index.CompoundIndex
import org.springframework.data.mongodb.core.index.CompoundIndexes
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@CompoundIndexes(
    CompoundIndex(
        name = "key_userId_index",
        def = "{'key': 1, 'userId': 1}",
        unique = false
    ),
    CompoundIndex(
        name = "key_token_unique_index",
        def = "{'key': 1, 'token': 1}",
        unique = true
    ),
    CompoundIndex(
        name = "key_token_userId_index",
        def = "{'key': 1, 'token': 1, 'userId': 1}",
        unique = false
    )
)
@Document("otp")
class OTP(
    id: Long? = null,
    val key: Keys,
    val token: String,
    val userId: Long,
    @Indexed(name = "expiry_date_ttl", expireAfterSeconds = 0)
    val expiryDate: Date,
) : HistoricalEntity(id) {
    enum class Keys(val subject: String, val template: String?) {
        EMAIL_VERIFICATION("Verification Mail", "email_verification"),
        DELETE_ACCOUNT("Delete Account Verification Mail", "delete_account_verification_mail"),
        REFRESH_TOKEN("Refresh Token", null),
        RESET_PASSWORD("Reset Password Mail", "reset_password")
    }
}
