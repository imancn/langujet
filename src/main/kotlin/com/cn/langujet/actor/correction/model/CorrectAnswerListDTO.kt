package com.cn.langujet.actor.correction.model

import com.cn.langujet.domain.correction.model.CorrectAnswer

data class CorrectAnswerListDTO(
    val examId: String? = null,
    val sectionOrder: Int? = null,
    val answers: List<CorrectAnswerDTO>? = null
) {

    inline fun <reified T : CorrectAnswer> toCorrectAnswer(): List<T> {
        return answers!!.map { it.toCorrectAnswer(examId!!, sectionOrder!!) }
    }

    companion object {
        fun fromCorrectAnswer(correctAnswer: List<CorrectAnswer>): List<CorrectAnswerListDTO> {
            return correctAnswer.groupBy { Pair(it.examId, it.sectionOrder) }.map { (key, answers) ->
                CorrectAnswerListDTO(key.first, key.second, answers.map { CorrectAnswerDTO.fromCorrectAnswer(it) })
            }
        }
    }
}