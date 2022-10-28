package com.cn.speaktest.repository.user

import com.cn.speaktest.model.security.ERole
import com.cn.speaktest.model.security.Role
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.*

interface RoleRepository : MongoRepository<Role, String> {
    fun findByName(name: ERole?): Optional<Role>
}