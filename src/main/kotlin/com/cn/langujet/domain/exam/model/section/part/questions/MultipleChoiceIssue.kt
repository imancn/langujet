package com.cn.langujet.domain.exam.model.section.part.questions

import org.springframework.data.annotation.TypeAlias

@TypeAlias("multiple_choice_issues")
data class MultipleChoiceIssue(
    var order: Int,
    var header: String,
    var description: String?,
    var options: List<String>,
)