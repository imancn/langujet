package com.cn.langujet.application.arch.models.entity

interface EntityInterface<ID> {
    fun id(): ID?
    fun id(id: ID?)
}
