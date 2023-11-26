package com.cn.langujet.domain.exam.model.question

class MultipleChoiceIssue(
    var index: Int,
    var header: String,
    var description: String?,
    var options: List<String>,
)