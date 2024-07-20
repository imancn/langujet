package com.cn.langujet.domain.correction.service

import com.cn.langujet.actor.correction.payload.dto.CorrectAnswerListDTO
import com.cn.langujet.application.advice.InvalidInputException
import com.cn.langujet.application.advice.NotFoundException
import com.cn.langujet.domain.correction.model.CorrectAnswerEntity
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
        ).firstOrNull() ?: throw InvalidInputException("Correct Answer list is empty")
    }

    fun updateCorrectAnswer(request: CorrectAnswerListDTO): CorrectAnswerListDTO {
        val entities = request.toCorrectAnswer<CorrectAnswerEntity>()
        entities.forEach {
            if (!repository.existsById(it.id ?: ""))
                throw NotFoundException("CorrectAnswer with id ${it.id} not found")
        }
        return CorrectAnswerListDTO.fromCorrectAnswer(
            repository.saveAll(
                request.toCorrectAnswer<CorrectAnswerEntity>()
            )
        ).firstOrNull() ?: throw InvalidInputException("Correct Answer list is empty")
    }

    fun getCorrectAnswer(
        examId: String,
        sectionOrder: Int,
        partOrder: Int?,
        questionOrder: Int?
    ): CorrectAnswerListDTO {
        return CorrectAnswerListDTO.fromCorrectAnswer(
            customRepository.findCorrectAnswersByOptionalCriteria(examId, sectionOrder, partOrder, questionOrder)
        ).firstOrNull() ?: throw NotFoundException("Correct Answer not found")
    }

    fun deleteCorrectAnswer(id: String) {
        if (!repository.existsById(id))
            throw NotFoundException("CorrectAnswer with id $id not found")
        repository.deleteById(id)
    }
}