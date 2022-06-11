package com.cn.speaktest.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document(collection = "refresh_tokens")
data class RefreshToken (
    @Id
    var id: String?,
    var user: User,
    var token: String,
    var expiryDate: Instant,
) {

}