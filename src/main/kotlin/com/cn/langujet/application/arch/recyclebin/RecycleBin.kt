package com.cn.langujet.application.arch.recyclebin

import com.cn.langujet.application.arch.models.entity.Entity
import com.cn.langujet.application.service.users.Auth
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.MongoOperations
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.stereotype.Service
import java.util.*

@Service
class RecycleBin {
    @field:Autowired
    lateinit var mongoOperations: MongoOperations

    fun drop(entity: Entity<*>): Boolean {
        mongoOperations.save(
            Trash(
                clazz = entity.javaClass,
                alias = entity.javaClass.getAnnotation(Document::class.java)?.collection ?: entity.javaClass.name,
                entity = entity,
                deletedAt = Date(),
                deletedBy = Auth.userId()
            )
        )
        return true
    }

    @Document("trash")
    private class Trash(
        @Autowired
        private val id: String? = null,
        private val clazz: Class<*>,
        private val alias: String,
        private val entity: Entity<*>,
        private val deletedAt: Date,
        private val deletedBy: Long
    )
}