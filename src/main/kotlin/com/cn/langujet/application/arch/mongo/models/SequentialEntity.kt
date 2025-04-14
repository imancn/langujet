package com.cn.langujet.application.arch.mongo.models

abstract class SequentialEntity() : HistoricalEntity<Long>() {
    constructor(id: Long?) : this() {
        this.id = id
    }
}