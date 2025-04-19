package com.cn.langujet.application.arch.controller.payload.response

import com.cn.langujet.application.arch.advice.UnprocessableException
import com.cn.langujet.application.arch.controller.payload.request.search.PageRequest
import org.springframework.data.domain.Page

data class PageResponse<T>(
    val content: List<T>,
    val pageNumber: Int,
    val pageSize: Int,
    val totalPages: Int,
    val totalElements: Long
) {
    constructor(
        content: List<T>,
        pageNumber: Int,
        pageSize: Int,
        totalElements: Long
    ): this(
        content = content,
        pageNumber = pageNumber,
        pageSize = pageSize,
        totalPages = if (pageSize > 0) ((totalElements + pageSize - 1) / pageSize).toInt() else throw UnprocessableException("pageSize must be bigger that 0"),
        totalElements = totalElements
    )
    
    constructor(
        content: List<T>,
        page: PageRequest,
        total: Long = (page.size * (page.number + 1)) + content.count().toLong()
    ): this(
        content = content,
        pageNumber = page.number,
        pageSize = page.size,
        totalPages = if (page.size > 0) ((total + page.size - 1) / page.size).toInt() else throw UnprocessableException("pageSize must be bigger that 0"),
        totalElements = total
    )
}

fun <T> List<T>.paginate(pageSize: Int, pageNumber: Int): PageResponse<T> {
    if (pageSize <= 0)
        throw UnprocessableException("pageSize must be bigger that 0")
    val total = this.size
    val start = pageNumber * pageSize
    val end = minOf(start + pageSize, total)
    val content = if (start <= total) this.subList(start, end) else emptyList()
    val totalPages = (total + pageSize - 1) / pageSize
    
    return PageResponse(
        content = content,
        totalElements = total.toLong(),
        pageNumber = pageNumber,
        pageSize = pageSize,
        totalPages = totalPages
    )
}

fun <T> Page<T>.toCustomPage(): PageResponse<T> {
    return PageResponse(
        content = this.content,
        totalElements = this.totalElements,
        pageNumber = this.number,
        pageSize = this.size,
        totalPages = this.totalPages
    )
}