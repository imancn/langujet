package com.cn.langujet.application.arch.models.entity

interface EntityInterface<ID : Any> {
    fun id(): ID
    fun id(id: ID?)
}
