package com.cn.langujet.domain.correction.service

import com.cn.langujet.actor.correction.payload.request.AssignCorrectionRequest
import com.cn.langujet.actor.correction.payload.request.AssignCorrectionToCorrectorRequest
import com.cn.langujet.actor.correction.payload.response.CorrectionResponse
import com.cn.langujet.actor.correction.payload.response.CorrectorAvailableCorrectionResponse
import com.cn.langujet.actor.util.Auth
import com.cn.langujet.application.advice.NotFoundException
import com.cn.langujet.application.advice.UnprocessableException
import com.cn.langujet.domain.correction.model.CorrectionEntity
import com.cn.langujet.domain.correction.model.CorrectionStatus
import com.cn.langujet.domain.correction.model.CorrectorType
import com.cn.langujet.domain.correction.repository.CorrectAnswerRepository
import com.cn.langujet.domain.correction.repository.CorrectionRepository
import com.cn.langujet.domain.correction.service.corrector.AutoCorrectorService
import com.cn.langujet.domain.exam.model.ExamSession
import com.cn.langujet.domain.exam.service.ExamVariantService
import com.cn.langujet.domain.exam.service.SectionService
import org.springframework.stereotype.Service

@Service
class CorrectionService(
    private val correctionRepository: CorrectionRepository,
    private val examVariantService: ExamVariantService,
    private val correctAnswerRepository: CorrectAnswerRepository,
    private val autoCorrectorService: AutoCorrectorService,
    private val sectionService: SectionService,
) {
    fun makeExamSessionCorrection(examSession: ExamSession) {
        val examVariant = examVariantService.getExamVariantById(examSession.examVariantId)
        val sections = sectionService.getSectionsMetaData(examSession.examId)
        val corrections = sections.map { section ->
            val correctorType = if (correctAnswerRepository.existsByExamIdAndSectionOrder(examSession.examId, section.order)) {
                CorrectorType.AUTO_CORRECTION
            } else {
                examVariant.correctorType
            }
            CorrectionEntity(
                correctorType,
                CorrectionStatus.PENDING,
                examSession.id ?: "",
                examVariant.examType,
                section.order,
                section.sectionType
            )
        }
        correctionRepository.saveAll(corrections).forEach {
            if (it.correctorType == CorrectorType.AUTO_CORRECTION)
                autoCorrectorService.correctExamSection(examSession, it)
        }
    }
    
    fun changeStatus(correctionEntity: CorrectionEntity, status: CorrectionStatus) {
        correctionEntity.status = status
        correctionEntity.refreshUpdatedDate()
        correctionRepository.save(correctionEntity)
    }
    
    fun getCorrectionsByExamSessionId(examSessionId: String): List<CorrectionEntity> {
        return correctionRepository.findAllByExamSessionId(examSessionId)
    }
    
    fun getCorrectionByExamSessionIdAndSectionOrder(examSessionId: String, sectionOrder: Int): CorrectionEntity {
        return correctionRepository.findAllByExamSessionIdAndSectionOrder(examSessionId, sectionOrder).orElseThrow {
            NotFoundException("Correction with examSessionId: $examSessionId and sectionOrder: $sectionOrder not found")
        }
    }
    
    fun areAllSectionCorrectionProcessed(examSessionId: String): Boolean {
        getCorrectionsByExamSessionId(examSessionId).forEach {
            if (it.status != CorrectionStatus.PROCESSED) return false
        }
        return true
    }
    
    private fun getCorrectorPendingCorrectionsPerExamSessionId(): Map<String, List<CorrectionEntity>> {
        return correctionRepository.findByCorrectorTypeAndStatusOrderByCreatedDateAsc(
            CorrectorType.HUMAN, CorrectionStatus.PENDING
        ).groupBy {
            it.examSessionId
        }
    }
    
    fun getCorrectorPendingCorrections(): List<CorrectorAvailableCorrectionResponse> {
        val responses = mutableListOf<CorrectorAvailableCorrectionResponse>()
        getCorrectorPendingCorrectionsPerExamSessionId().forEach { (examSessionId, corrections) ->
            val sectionTypes = corrections.map { it.sectionType }
            val response = responses.find {
                it.examType == corrections[0].examType &&
                    it.sectionTypes == sectionTypes
            }?.also {
                it.count++
            }
            if (response == null) {
                responses += CorrectorAvailableCorrectionResponse(
                    corrections[0].examType,
                    sectionTypes,
                    1
                )
            }
        }
        return responses
    }
    
    fun assignCorrection(
        assignCorrectionRequest: AssignCorrectionRequest,
        correctorId: String = Auth.userId()
    ): CorrectionResponse {
        // TODO: validate corrector has not processing correction
        return try {
            val foundedCorrections = getCorrectorPendingCorrectionsPerExamSessionId().filter {
                val sectionTypesCondition = it.value.map {
                    correction -> correction.sectionType
                }.sorted() == assignCorrectionRequest.sectionTypes.sorted()
                val examTypeCondition = it.value.first().examType == assignCorrectionRequest.examType
                sectionTypesCondition && examTypeCondition
            }.firstNotNullOf {
                it.value
            }
            correctionRepository.saveAll(
                foundedCorrections.onEach {
                    it.correctorUserId = correctorId
                    it.status = CorrectionStatus.PROCESSING
                }
            )
            CorrectionResponse(
                foundedCorrections.first().examType,
                foundedCorrections.map { correction -> correction.sectionType }
            )
        } catch (ex: NoSuchElementException) {
            throw UnprocessableException("There is no exam of this type left for correction")
        }
    }
    
    fun assignCorrectionToCorrector(
        assignCorrectionToCorrectorRequest: AssignCorrectionToCorrectorRequest
    ): CorrectionResponse {
        // TODO: validate corrector exists and has corrector
        return assignCorrection(
            AssignCorrectionRequest(
                assignCorrectionToCorrectorRequest.examType,
                assignCorrectionToCorrectorRequest.sectionTypes
            ),
            assignCorrectionToCorrectorRequest.correctorId
        )
    }
}
