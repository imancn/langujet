package com.cn.langujet.application.arch.controller.payload.request.search

data class SearchRequest(
    val filters: FilterRequest? = null,
    val sorts: List<SortRequest>? = null,
    val page: PageRequest = PageRequest()
)
