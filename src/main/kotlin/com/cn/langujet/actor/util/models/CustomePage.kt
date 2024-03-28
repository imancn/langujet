package com.cn.langujet.actor.util.models

import org.springframework.data.domain.Page

data class CustomPage<T>(
    val content: List<T>,
    val pageSize: Int,
    val pageNumber: Int,
    val totalPages: Int,
    val totalElements: Long
)

fun <T> List<T>.paginate(pageSize: Int, pageNumber: Int): CustomPage<T> {
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