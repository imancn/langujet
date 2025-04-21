package com.cn.langujet.application.service.cms.enums

import com.cn.langujet.application.arch.models.entity.HistoricalEntity
import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.core.index.CompoundIndex
import org.springframework.data.mongodb.core.index.CompoundIndexes
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

@TypeAlias(value = "enums")
@Document(collection = "enums")
@CompoundIndexes(
    CompoundIndex(
        name = "unique_clazz_language_key_index",
        def = "{'clazz': 1, 'language': 1, 'key': 1}",
        unique = true
    ),
    CompoundIndex(
        name = "clazz_language_index",
        def = "{'clazz': 1, 'language': 1}",
        unique = false
    )
)
class EnumEntity(
    id: Long?,
    
    @Indexed(name = "clazz_index", unique = false)
    val clazz: String,
    val language: Languages,
    val key: String,
    
    val value: String,
    val color: String?,
    val iconFileId: Long?,
    val alt: String?,
): HistoricalEntity(id = id)