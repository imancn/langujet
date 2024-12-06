package com.cn.langujet.domain.answer

import com.cn.langujet.actor.answer.payload.request.*
import com.cn.langujet.application.advice.UnprocessableException
import com.cn.langujet.application.service.file.domain.data.model.FileBucket
import com.cn.langujet.application.service.file.domain.service.FileService
import com.cn.langujet.domain.answer.model.AnswerEntity
import com.cn.langujet.domain.exam.model.ExamSessionState
import com.cn.langujet.domain.exam.service.ExamSessionService
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class AnswerService(
    private val answerRepository: AnswerRepository,
    private val examSessionService: ExamSessionService,
    private val fileService: FileService
) {
    fun submitVoiceAnswer(
        examSessionId: String,
        sectionOrder: Int,
        partOrder: Int,
        questionOrder: Int,
        voice: MultipartFile
    ): AnswerEntity.VoiceAnswerEntity {
        examSessionPreCheck(examSessionId)
        val fileEntity = fileService.uploadFile(voice, FileBucket.ANSWERS)
        
        if (answerRepository.existsByExamSessionIdAndSectionOrderAndPartOrderAndQuestionOrder(
                examSessionId ,sectionOrder ,partOrder ,questionOrder)
        ) {
            throw UnprocessableException("You have submitted this answer once")
        }
        
        return answerRepository.save(
            AnswerEntity.VoiceAnswerEntity(
                examSessionId,
                sectionOrder,
                partOrder,
                questionOrder,
                fileEntity.id!!
            )
        )
    }
    
    fun submitBulkAnswers(
        examSessionId: String?,
        sectionOrder: Int?,
        answerRequestList: List<AnswerBulkRequest>,
    ): Boolean {
        examSessionPreCheck(examSessionId!!)
        val existingAnswers = answerRepository.findAllByExamSessionIdAndSectionOrder(examSessionId, sectionOrder!!)
        answerRepository.saveAll(
            answerRequestList.map<AnswerBulkRequest, AnswerEntity> {
                convertAnswerBulkRequestToAnswer(it, examSessionId, sectionOrder)
            }.onEach { answer ->
                answer.id = existingAnswers.find {
                    (answer.partOrder == it.partOrder).and(answer.questionOrder == it.questionOrder)
                }?.id
            }
        )
        return true
    }
    
    private inline fun <reified T : AnswerEntity> convertAnswerBulkRequestToAnswer(
        answerRequest: AnswerBulkRequest,
        examSessionId: String,
        sectionOrder: Int
    ): T {
        val answer: AnswerEntity = when (answerRequest) {
            is TextBulkAnswerRequest -> AnswerEntity.TextAnswerEntity(
                examSessionId,
                sectionOrder,
                answerRequest.partOrder!!,
                answerRequest.questionOrder!!,
                answerRequest.text!!
            )
            
            is TextIssuesBulkAnswerRequest -> AnswerEntity.TextIssuesAnswerEntity(
                examSessionId,
                sectionOrder,
                answerRequest.partOrder!!,
                answerRequest.questionOrder!!,
                answerRequest.issues!!
            )
            
            is TrueFalseBulkAnswerRequest -> AnswerEntity.TrueFalseAnswerEntity(
                examSessionId,
                sectionOrder,
                answerRequest.partOrder!!,
                answerRequest.questionOrder!!,
                answerRequest.issues!!
            )
            
            is MultipleChoiceBulkAnswerRequest -> AnswerEntity.MultipleChoiceAnswerEntity(
                examSessionId,
                sectionOrder,
                answerRequest.partOrder!!,
                answerRequest.questionOrder!!,
                answerRequest.issues!!.mapNotNull {
                    it?.let { multipleChoiceIssueAnswerRequest ->
                        AnswerEntity.MultipleChoiceIssueAnswer(
                            multipleChoiceIssueAnswerRequest.issueOrder!!,
                            multipleChoiceIssueAnswerRequest.options!!
                        )
                    }
                }
            )
            
            else -> throw IllegalArgumentException("Unsupported answer request type")
        }
        if (answer !is T) {
            throw IllegalArgumentException("The answer type does not match the expected return type.")
        }
        return answer
    }
    
    private fun examSessionPreCheck(examSessionId: String) {
        val examSession = examSessionService.getStudentExamSession(examSessionId)
        if (examSession.state.order == ExamSessionState.ENROLLED.order)
            throw UnprocessableException("The exam session has been not started yet")
        if (examSession.state.order >= ExamSessionState.FINISHED.order)
            throw UnprocessableException("The exam session has been finished")
    }
}