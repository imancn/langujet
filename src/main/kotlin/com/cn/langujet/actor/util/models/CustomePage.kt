package com.cn.langujet.actor.util.models

import com.cn.langujet.application.advice.UnprocessableException
import org.springframework.data.domain.Page

data class CustomPage<T>(
    val content: List<T>,
    val pageSize: Int,
    val pageNumber: Int,
    val totalPages: Int,
    val totalElements: Long
) {
    constructor(
        content: List<T>,
        pageSize: Int,
        pageNumber: Int,
        totalElements: Long
    ): this(
        content = content,
        pageSize = pageSize,
        pageNumber = pageNumber,
        totalPages = if (pageSize > 0) ((totalElements + pageSize - 1) / pageSize).toInt() else throw UnprocessableException("pageSize must be bigger that 0"),
        totalElements = totalElements
    )
}

fun <T> List<T>.paginate(pageSize: Int, pageNumber: Int): CustomPage<T> {
    if (pageSize <= 0)
        throw UnprocessableException("pageSize must be bigger that 0")
    val total = this.size
    val start = pageNumber * pageSize
    val end = minOf(start + pageSize, total)
    val content = if (start <= total) this.subList(start, end) else emptyList()
    val totalPages = (total + pageSize - 1) / pageSize
    
    return CustomPage(
        content = content,
        totalElements = total.toLong(),
        pageNumber = pageNumber,
        pageSize = pageSize,
        totalPages = totalPages
    )
}

fun <T> Page<T>.toCustomPage(): CustomPage<T> {
    return CustomPage(
        content = this.content,
        totalElements = this.totalElements,
        pageNumber = this.number,
        pageSize = this.size,
        totalPages = this.totalPages
    )
}