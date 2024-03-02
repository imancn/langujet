package com.cn.langujet.domain.exam.model.question

class MultipleChoiceIssue(
    var order: Int,
    var header: String,
    var description: String?,
    var options: List<String>,
)