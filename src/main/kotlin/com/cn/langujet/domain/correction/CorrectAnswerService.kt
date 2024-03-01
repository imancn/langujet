package com.cn.langujet.domain.correction

import com.cn.langujet.actor.correction.model.CorrectAnswerListDTO
import com.cn.langujet.application.advice.NotFoundException
import com.cn.langujet.domain.correction.model.CorrectAnswer
import com.cn.langujet.domain.correction.repository.CorrectAnswerCustomRepository
import com.cn.langujet.domain.correction.repository.CorrectAnswerRepository
import org.springframework.stereotype.Service

@Service
class CorrectAnswerService(
    private val repository: CorrectAnswerRepository,
    private val customRepository: CorrectAnswerCustomRepository
) {
    fun createCorrectAnswer(request: CorrectAnswerListDTO): CorrectAnswerListDTO {
        return CorrectAnswerListDTO.fromCorrectAnswer(
            repository.saveAll(request.toCorrectAnswer())
        ).first()
    }

    fun updateCorrectAnswer(request: CorrectAnswerListDTO): CorrectAnswerListDTO {
        val entities = request.toCorrectAnswer<CorrectAnswer>()
        entities.forEach {
            if (!repository.existsById(it.id ?: ""))
                throw NotFoundException("CorrectAnswer with id ${it.id} not found")
        }
        return CorrectAnswerListDTO.fromCorrectAnswer(
            repository.saveAll(
                request.toCorrectAnswer<CorrectAnswer>()
            )
        ).first()
    }

    fun getCorrectAnswer(
        examId: String,
        sectionOrder: Int,
        partId: Int?,
        questionId: Int?
    ): CorrectAnswerListDTO {
        return CorrectAnswerListDTO.fromCorrectAnswer(
            customRepository.findCorrectAnswersByOptionalCriteria(examId, sectionOrder, partId, questionId)
        ).first()
    }

    fun deleteCorrectAnswer(id: String) {
        if (!repository.existsById(id))
            throw NotFoundException("CorrectAnswer with id $id not found")
        repository.deleteById(id)
    }
}