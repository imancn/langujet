package com.cn.langujet.domain.result.service

import com.cn.langujet.actor.result.payload.ResultDto
import com.cn.langujet.application.advice.InvalidTokenException
import com.cn.langujet.application.advice.MethodNotAllowedException
import com.cn.langujet.application.advice.NotFoundException
import com.cn.langujet.domain.correction.model.CorrectionStatus
import com.cn.langujet.domain.correction.service.CorrectionService
import com.cn.langujet.domain.exam.model.ExamType
import com.cn.langujet.domain.exam.service.ExamSessionService
import com.cn.langujet.domain.result.model.Result
import com.cn.langujet.domain.result.model.SectionResult
import com.cn.langujet.domain.result.repository.ResultRepository
import com.cn.langujet.domain.student.service.StudentService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Service

@Service
class ResultService(
    private val resultRepository: ResultRepository,
    private val studentService: StudentService,
    private val correctionService: CorrectionService,
    private val sectionResultService: SectionResultService
) {
    @Autowired
    @Lazy
    private lateinit var examSessionService: ExamSessionService
    
    fun initiateResult(examSessionId: String, examType: ExamType) {
        resultRepository.save(
            Result(
                id = null,
                examSessionId = examSessionId,
                examType = examType,
                score = null,
                recommendation = null,
            )
        )
    }
    
    fun getResultById(id: String): Result {
        return resultRepository.findById(id)
            .orElseThrow { throw NotFoundException("Suggestion with ID: $id not found") }
    }
    
    fun getResultByExamSessionId(
        authToken: String, examSessionId: String
    ): ResultDto {
        val studentId = examSessionService.getExamSessionById(examSessionId).studentId
        if (!studentService.doesStudentOwnAuthToken(
                authToken,
                studentId
            )
        ) throw InvalidTokenException("Exam Session with id: $examSessionId is not belong to your token")
        return resultRepository.findByExamSessionId(examSessionId).orElseThrow {
            NotFoundException("Suggestions for ExamSession with ID: $examSessionId not found")
        }.let { ResultDto(it) }
    }
    
    fun getResultByExamSessionId(examSessionId: String): Result {
        return resultRepository.findByExamSessionId(examSessionId).orElseThrow {
            NotFoundException("Suggestions for ExamSession with ID: $examSessionId not found")
        }
    }
    
    fun addSectionResult(result: Result, sectionResult: SectionResult) {
        val correction = correctionService.getCorrectionByExamSessionIdAndSectionOrder(
            result.examSessionId,
            sectionResult.sectionOrder
        )
        if (correction.status == CorrectionStatus.PROCESSED)
            throw MethodNotAllowedException("Section with ExamSessionId: ${result.examSessionId} and SectionOrder: ${sectionResult.sectionOrder} has been processed")
        sectionResultService.createSectionResult(sectionResult)
        correctionService.changeStatus(correction, CorrectionStatus.PROCESSED)
        examSessionService.determineCorrectionStatus(result.examSessionId)
    }
}