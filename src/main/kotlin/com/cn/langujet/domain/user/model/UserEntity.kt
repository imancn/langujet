package com.cn.langujet.domain.user.model

import com.cn.langujet.application.shared.entity.HistoricalEntity
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "users")
@TypeAlias("users")
data class UserEntity(
    @Id
    var id: String?,
    @Indexed(name = "unique_users_index", unique = true)
    var standardEmail: String,
    var email: String,
    var emailVerified: Boolean,
    var password: String? = null,
    var roles: Set<Role> = HashSet(),
    var deleted: Boolean = false
): HistoricalEntity()