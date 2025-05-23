package com.cn.langujet.domain.correction.service

import com.cn.langujet.actor.correction.payload.dto.CorrectAnswerListDTO
import com.cn.langujet.application.arch.advice.InvalidInputException
import com.cn.langujet.application.arch.advice.UnprocessableException
import com.cn.langujet.application.arch.mongo.HistoricalEntityService
import com.cn.langujet.domain.correction.model.CorrectAnswerEntity
import com.cn.langujet.domain.correction.repository.CorrectAnswerCustomRepository
import com.cn.langujet.domain.correction.repository.CorrectAnswerRepository
import com.cn.langujet.domain.exam.service.PartService
import com.cn.langujet.domain.exam.service.QuestionService
import com.cn.langujet.domain.exam.service.SectionService
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.stereotype.Service

@Service
class CorrectAnswerService(
    override var repository: CorrectAnswerRepository,
    private val customRepository: CorrectAnswerCustomRepository,
    private val sectionService: SectionService,
    private val partService: PartService,
    private val questionService: QuestionService
) : HistoricalEntityService<CorrectAnswerRepository, CorrectAnswerEntity>() {
    fun createCorrectAnswer(request: CorrectAnswerListDTO): CorrectAnswerListDTO {
        request.answers?.forEach { it.id = null }
        validate(request)
        return CorrectAnswerListDTO.fromCorrectAnswer(
            createMany(request.toCorrectAnswer())
        ).firstOrNull() ?: throw InvalidInputException("Correct Answer list is empty")
    }

    fun updateCorrectAnswer(request: CorrectAnswerListDTO): CorrectAnswerListDTO {
        validate(request)
        return CorrectAnswerListDTO.fromCorrectAnswer(
            request.toCorrectAnswer<CorrectAnswerEntity>().map {
                save(it)
            }
        ).firstOrNull() ?: throw InvalidInputException("Correct Answer list is empty")
    }
    
    fun getSectionCorrectAnswers(
        examId: Long,
        sectionOrder: Int
    ) = repository.findAllByExamIdAndSectionId(examId, sectionOrder)

    fun getCorrectAnswer(
        examId: Long,
        sectionOrder: Int,
        partOrder: Int?,
        questionOrder: Int?
    ): CorrectAnswerListDTO {
        return CorrectAnswerListDTO.fromCorrectAnswer(
            customRepository.findCorrectAnswersByOptionalCriteria(examId, sectionOrder, partOrder, questionOrder)
        ).firstOrNull() ?: throw UnprocessableException("Correct Answer not found")
    }
    
    fun deleteCorrectAnswer(id: Long) {
        if (!repository.existsById(id))
            throw UnprocessableException("CorrectAnswer with id $id not found")
        repository.deleteById(id)
    }
    
    private fun validate(request: CorrectAnswerListDTO) {
        val section = sectionService.getSectionByExamIdAndOrder(request.examId!!, request.sectionOrder!!)
        val existingCorrectAnswers = getSectionCorrectAnswers(section.examId, section.order)
        
        request.answers?.forEach { correctAnswer ->
            val part = partService.find(
                Criteria.where("examId").`is`(section.examId)
                    .and("sectionId").`is`(section.id)
                    .and("order").`is`(correctAnswer.partOrder)
            ).firstOrNull() ?: throw UnprocessableException("There is no part with order: ${correctAnswer.partOrder}")
            
            val question = questionService.find(
                Criteria.where("examId").`is`(section.examId)
                    .and("sectionId").`is`(section.id)
                    .and("partId").`is`(part.id)
                    .and("questionOrder").`is`(correctAnswer.questionOrder)
            ).firstOrNull()
                ?: throw UnprocessableException("There is no question with part order: ${correctAnswer.partOrder} and question order ${correctAnswer.questionOrder}")
            
            if (correctAnswer.id == null) { /// just for create
                val doesExist = repository.existsByExamIdAndSectionIdAndPartIdAndQuestionId(
                    request.examId,
                    request.sectionOrder,
                    correctAnswer.partOrder!!,
                    correctAnswer.questionOrder!!
                )
                if (doesExist) throw UnprocessableException("Correct Answer with exam id: ${request.examId} and section order: ${request.sectionOrder} and part order: ${correctAnswer.partOrder} and question order: ${correctAnswer.questionOrder} already exists")
            } else { /// just for update
                if (existingCorrectAnswers.find { it.id == correctAnswer.id } == null)
                    throw UnprocessableException("CorrectAnswer with id ${correctAnswer.id} not found")
            }
        }
        
        val correctAnswers = request.toCorrectAnswer<CorrectAnswerEntity>().toMutableList()
        existingCorrectAnswers.forEach { existing ->
            if (correctAnswers.find { new -> new.id == existing.id } == null) {
                correctAnswers.add(existing)
            }
        }
        correctAnswers.groupBy { "${it.sectionId}-${it.partId}-${it.questionId}" }.forEach {
            if (it.value.size > 1) {
                throw UnprocessableException("Duplicate correct answer for examId: ${it.value.first().examId} and section order: ${it.value.first().sectionId} and part order: ${it.value.first().partId} and question order: ${it.value.first().questionId}")
            }
        }
    }
}