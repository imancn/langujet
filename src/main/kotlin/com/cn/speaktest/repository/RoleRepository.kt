package com.cn.speaktest.repository

import com.cn.speaktest.model.ERole
import com.cn.speaktest.model.Role
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.*

interface RoleRepository : MongoRepository<Role, String> {
    fun findByName(name: ERole?): Optional<Role>
}