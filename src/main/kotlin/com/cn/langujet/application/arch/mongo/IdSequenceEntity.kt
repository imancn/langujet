package com.cn.langujet.application.arch.mongo

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Version
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "id_sequences")
data class IdSequenceEntity(
    @Id
    val name: String,
    
    var seq: Long,
    
    @Version
    var version: Long
)