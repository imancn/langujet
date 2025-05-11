package com.cn.langujet.application.service.otp

import com.cn.langujet.application.arch.mongo.HistoricalEntityService
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Service
import java.util.*
import kotlin.random.Random

@Service
class OTPService(
    override var repository: OTPRepository
) : HistoricalEntityService<OTPRepository, OTP>() {

    fun isValid(key: OTP.Keys, token: String, userId: Long): Boolean {
        val criteria = Criteria
            .where("key").`is`(key)
            .and("token").`is`(token.uppercase())
            .and("userId").`is`(userId)

        val query = Query(criteria)
        return mongoOperations.exists(query, OTP::class.java)
    }

    fun findByToken(key: OTP.Keys, token: String): OTP? {
        val criteria = Criteria
            .where("key").`is`(key)
            .and("token").`is`(token.uppercase())

        val query = Query(criteria)
        return mongoOperations.find(query, OTP::class.java).getOrNull(0)
    }

    fun findByUserId(key: OTP.Keys, userId: Long): OTP? {
        val criteria = Criteria
            .where("key").`is`(key)
            .and("userId").`is`(userId)

        val query = Query(criteria)
        return mongoOperations.find(query, OTP::class.java).getOrNull(0)
    }

    fun generate(
        key: OTP.Keys,
        userId: Long,
        ttl: Long = 5 * 60 * 1000,
        len: Int = 6,
        numeric: Boolean = true,
        characters: Boolean = false,
    ): OTP {
        val chars = when {
            numeric && characters -> ('0'..'9') + ('A'..'Z')
            numeric -> ('0'..'9').toList()
            characters -> ('A'..'Z').toList()
            else -> throw IllegalArgumentException("Either numeric or characters must be true.")
        }

        return create(
            OTP(
                id = null,
                key = key,
                token = List(len) { chars[Random.nextInt(chars.size)] }.joinToString(""),
                userId = userId,
                expiryDate = Date(System.currentTimeMillis() + ttl)
            )
        )
    }

    fun findOrGenerate(key: OTP.Keys, userId: Long): OTP {
        return findByUserId(key, userId) ?: generate(key, userId)
    }

    fun invalidate(otp: OTP) {
    }
}
