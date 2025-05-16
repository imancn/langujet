package com.cn.langujet.domain.user.model

import com.cn.langujet.application.arch.models.entity.HistoricalEntity
import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "users")
@TypeAlias("users")
class UserEntity(
    id: Long?,
    @Indexed(name = "unique_username_index", unique = true)
    var username: String,
    var email: String,
    var emailVerified: Boolean,
    var password: String? = null,
    var roles: Set<Role> = HashSet(),
    var deleted: Boolean = false
) : HistoricalEntity(id = id)