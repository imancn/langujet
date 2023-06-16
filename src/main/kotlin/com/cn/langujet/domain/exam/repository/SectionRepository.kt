package com.cn.langujet.domain.exam.repository

import com.cn.langujet.domain.exam.model.Section
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface SectionRepository : MongoRepository<Section, String>