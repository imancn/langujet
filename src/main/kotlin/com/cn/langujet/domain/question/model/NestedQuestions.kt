package com.cn.langujet.domain.question.model

class MultipleChoiceIssue(
    var header: String,
    var description: String?,
    var options: List<String>,
)