package com.cn.langujet.domain.professor

import org.springframework.data.mongodb.repository.MongoRepository
import java.util.Optional

interface ProfessorRepository : MongoRepository<Professor, String> {
    fun findByUser_Id(userId: String): Optional<Professor>
}