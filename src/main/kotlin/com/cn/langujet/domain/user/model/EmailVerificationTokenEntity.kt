package com.cn.langujet.domain.user.model

import com.cn.langujet.application.arch.models.entity.HistoricalEntity
import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document(collection = "email_verification_tokens")
@TypeAlias("email_verification_tokens")
class EmailVerificationTokenEntity(
    id: Long?,
    @DBRef var user: UserEntity,
    var token: String,
    @Indexed(name = "expiry_date_ttl", expireAfterSeconds = 0)
    var expiryDate: Date,
) : HistoricalEntity(id = id) {
    constructor(user: UserEntity) : this(
        id = null, user = user, token = makeRandom6DigitsToken(), expiryDate = calculateExpiryDate()
    )

    companion object {
        private const val EXPIRATION = 15

        private fun makeRandom6DigitsToken(): String {
            val digit = Random().nextInt(899_999) + 100_000
            return String.format("%06d", digit)
        }

        private fun calculateExpiryDate(expiryTimeInMinutes: Int = EXPIRATION): Date {
            val cal = Calendar.getInstance()
            cal.time = Date(cal.time.time)
            cal.add(Calendar.MINUTE, expiryTimeInMinutes)
            return Date(cal.time.time)
        }
    }
}