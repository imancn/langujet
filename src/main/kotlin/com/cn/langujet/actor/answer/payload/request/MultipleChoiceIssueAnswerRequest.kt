package com.cn.langujet.actor.answer.payload.request

import com.cn.langujet.domain.answer.model.Answer
import jakarta.validation.constraints.NotNull

class MultipleChoiceIssueAnswerRequest(
    @field:NotNull var issueOrder: Int? = null,
    @field:NotNull var options: List<String?>? = null
) {
    fun toMultipleChoiceIssueAnswer(): Answer.MultipleChoiceIssueAnswer {
        return Answer.MultipleChoiceIssueAnswer(
            this.issueOrder!!,
            this.options!!
        )
    }
}