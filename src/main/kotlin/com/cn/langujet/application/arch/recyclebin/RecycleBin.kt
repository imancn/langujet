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
                className = entity.javaClass.name,
                alias = entity.javaClass.getAnnotation(Document::class.java)?.collection ?: entity.javaClass.name,
                entity = entity,
                deletedAt = Date(),
                deletedBy = Auth.userId()
            )
        )
        return true
    }

    @Document("trash")
    class Trash(
        @Autowired
        var id: String? = null,
        var className: String,
        var alias: String,
        var entity: Entity<*>,
        var deletedAt: Date,
        var deletedBy: Long
    )
}