package com.cn.langujet.domain.correction.service

import com.cn.langujet.actor.correction.payload.dto.CorrectAnswerListDTO
import com.cn.langujet.application.advice.InvalidInputException
import com.cn.langujet.application.advice.NotFoundException
import com.cn.langujet.application.advice.UnprocessableException
import com.cn.langujet.domain.correction.model.CorrectAnswerEntity
import com.cn.langujet.domain.correction.repository.CorrectAnswerCustomRepository
import com.cn.langujet.domain.correction.repository.CorrectAnswerRepository
import com.cn.langujet.domain.exam.service.SectionService
import org.springframework.stereotype.Service

@Service
class CorrectAnswerService(
    private val repository: CorrectAnswerRepository,
    private val customRepository: CorrectAnswerCustomRepository,
    private val sectionService: SectionService
) {
    fun createCorrectAnswer(request: CorrectAnswerListDTO): CorrectAnswerListDTO {
        request.answers?.forEach { it.id = null }
        validate(request)
        return CorrectAnswerListDTO.fromCorrectAnswer(
            repository.saveAll(request.toCorrectAnswer())
        ).firstOrNull() ?: throw InvalidInputException("Correct Answer list is empty")
    }

    fun updateCorrectAnswer(request: CorrectAnswerListDTO): CorrectAnswerListDTO {
        validate(request)
        return CorrectAnswerListDTO.fromCorrectAnswer(
            repository.saveAll(
                request.toCorrectAnswer<CorrectAnswerEntity>()
            )
        ).firstOrNull() ?: throw InvalidInputException("Correct Answer list is empty")
    }
    
    fun getSectionCorrectAnswers(
        examId: String,
        sectionOrder: Int
    ) = repository.findAllByExamIdAndSectionOrder(examId, sectionOrder)

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
    
    private fun validate(request: CorrectAnswerListDTO) {
        val section = sectionService.getSectionByExamIdAndOrder(request.examId!!, request.sectionOrder!!)
        val existingCorrectAnswers = getSectionCorrectAnswers(section.examId, section.order)
        
        request.answers?.forEach { correctAnswer ->
            val part = section.parts.find { part -> part.order == correctAnswer.partOrder }
                ?: throw UnprocessableException("There is no part with order: ${correctAnswer.partOrder}")
            
            part.getQuestions().find { question -> question.order == correctAnswer.questionOrder }
                ?: throw UnprocessableException("There is no question with part order: ${correctAnswer.partOrder} and question order ${correctAnswer.questionOrder}")
            
            if (correctAnswer.id == null) { /// just for create
                val doesExist = repository.existsByExamIdAndSectionOrderAndPartOrderAndQuestionOrder(
                    request.examId,
                    request.sectionOrder,
                    correctAnswer.partOrder!!,
                    correctAnswer.questionOrder!!
                )
                if (doesExist) throw UnprocessableException("Correct Answer with exam id: ${request.examId} and section order: ${request.sectionOrder} and part order: ${correctAnswer.partOrder} and question order: ${correctAnswer.questionOrder} already exists")
            } else { /// just for update
                if (existingCorrectAnswers.find { it.id == correctAnswer.id } == null)
                    throw NotFoundException("CorrectAnswer with id ${correctAnswer.id} not found")
            }
        }
        
        val correctAnswers = request.toCorrectAnswer<CorrectAnswerEntity>().toMutableList()
        existingCorrectAnswers.forEach { existing ->
            if (correctAnswers.find { new -> new.id == existing.id } == null) {
                correctAnswers.add(existing)
            }
        }
        correctAnswers.groupBy { "${it.sectionOrder}-${it.partOrder}-${it.questionOrder}" }.forEach {
            if (it.value.size > 1) {
                throw UnprocessableException("Duplicate correct answer for examId: ${it.value.first().examId} and section order: ${it.value.first().sectionOrder} and part order: ${it.value.first().partOrder} and question order: ${it.value.first().questionOrder}")
            }
        }
    }
}