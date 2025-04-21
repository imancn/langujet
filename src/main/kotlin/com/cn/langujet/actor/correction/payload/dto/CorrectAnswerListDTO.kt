package com.cn.langujet.actor.correction.payload.dto

import com.cn.langujet.domain.correction.model.CorrectAnswerEntity

data class CorrectAnswerListDTO(
    val examId: Long? = null,
    val sectionOrder: Int? = null,
    val answers: List<CorrectAnswerDTO>? = null
) {

    inline fun <reified T : CorrectAnswerEntity> toCorrectAnswer(): List<T> {
        return answers!!.map { it.toCorrectAnswer(examId!!, sectionOrder!!) }
    }

    companion object {
        fun fromCorrectAnswer(correctAnswer: List<CorrectAnswerEntity>): List<CorrectAnswerListDTO> {
            return correctAnswer.groupBy { Pair(it.examId, it.sectionOrder) }.map { (key, answers) ->
                CorrectAnswerListDTO(key.first, key.second, answers.map { CorrectAnswerDTO.fromCorrectAnswer(it) })
            }
        }
    }
}