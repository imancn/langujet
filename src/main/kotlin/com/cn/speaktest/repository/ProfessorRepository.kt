package com.cn.speaktest.repository

import com.cn.speaktest.model.Professor
import org.springframework.data.mongodb.repository.MongoRepository

interface ProfessorRepository : MongoRepository<Professor, String>