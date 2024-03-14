package com.cn.langujet.domain.answer

import com.cn.langujet.actor.answer.payload.request.AnswerBulkRequest
import com.cn.langujet.application.service.file.domain.data.model.FileBucket
import com.cn.langujet.application.service.file.domain.service.FileService
import com.cn.langujet.domain.answer.model.Answer
import com.cn.langujet.domain.exam.service.ExamSessionService
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.util.*

@Service
class AnswerService(
    private val answerRepository: AnswerRepository,
    private val examSessionService: ExamSessionService,
    private val fileService: FileService
) {

    fun submitBulkAnswers(
        examSessionId: String?,
        sectionOrder: Int?,
        answerRequestList: List<AnswerBulkRequest>,
        auth: String
    ): Boolean {
        examSessionPreCheck(examSessionId!!, sectionOrder!!, auth)
        answerRepository.saveAll(
            answerRequestList.map {
                it.convertToAnswer(examSessionId, sectionOrder)
            }
        )
        return true
    }

    fun submitVoiceAnswer(
        token: String,
        examSessionId: String,
        sectionOrder: Int,
        partOrder: Int,
        questionOrder: Int,
        voice: MultipartFile
    ): Answer.VoiceAnswer {
        examSessionPreCheck(examSessionId, sectionOrder, token)
        val fileEntity = fileService.uploadFile(voice, FileBucket.ANSWERS)
        return answerRepository.save(
            Answer.VoiceAnswer(
                examSessionId,
                sectionOrder,
                partOrder,
                questionOrder,
                Date(System.currentTimeMillis()),
                fileEntity.id!!
            )
        )
    }

    private fun examSessionPreCheck(examSessionId: String, sectionOrder: Int, token: String) {
        examSessionService.checkExamSessionAndSectionAvailability(
            examSessionService.getStudentExamSession(token, examSessionId), sectionOrder
        )
    }
}