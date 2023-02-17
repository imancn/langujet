package com.cn.speaktest.professor

import org.springframework.data.mongodb.repository.MongoRepository

interface ProfessorRepository : MongoRepository<Professor, String>