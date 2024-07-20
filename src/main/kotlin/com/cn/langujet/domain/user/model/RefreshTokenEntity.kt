package com.cn.langujet.domain.user.model

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.util.Date

@Document(collection = "refresh_tokens")
@TypeAlias("refresh_tokens")
data class RefreshTokenEntity(
    @Id
    var id: String?,
    var userId: String,
    @Indexed(expireAfterSeconds = 0)
    var expiryDate: Date,
)