package com.cn.langujet.domain.exam.service

import com.cn.langujet.actor.exam.payload.ExamContentDownloadLink
import com.cn.langujet.application.service.users.Auth
import com.cn.langujet.application.arch.advice.InternalServerError
import com.cn.langujet.application.arch.advice.InvalidCredentialException
import com.cn.langujet.application.arch.advice.UnprocessableException
import com.cn.langujet.application.arch.models.entity.Entity
import com.cn.langujet.application.arch.mongo.HistoricalEntityService
import com.cn.langujet.application.service.file.domain.data.model.FileBucket
import com.cn.langujet.application.service.file.domain.service.FileService
import com.cn.langujet.domain.exam.model.ExamContentEntity
import com.cn.langujet.domain.exam.repository.ExamContentRepository
import com.cn.langujet.domain.exam.repository.ExamSessionRepository
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.util.regex.Pattern
import kotlin.jvm.optionals.getOrNull

@Service
class ExamContentService(
    private val examContentRepository: ExamContentRepository,
    private val examSessionRepository: ExamSessionRepository,
    private val sectionService: SectionService,
    private val examService: ExamService,
    private val fileService: FileService
) : HistoricalEntityService<ExamContentRepository, ExamContentEntity>() {
    fun uploadExamContent(
        file: MultipartFile, examId: Long, sectionOrder: Int?, partOrder: Int?, questionOrder: Int?
    ): ExamContentEntity {
        sectionService.getSectionsByExamId(examId).find {
            it.order == sectionOrder
        } ?: throw UnprocessableException("Section not found")
        
        val fileEntity = fileService.uploadFile(file, FileBucket.EXAM_CONTENTS)
        
        return save(
            ExamContentEntity(
                null,
                examId,
                sectionOrder,
                partOrder,
                questionOrder,
                fileEntity.id ?: throw InternalServerError("Upload Failed")
            )
        )
    }
    
    fun getAdminExamContentDownloadLink(
        examId: Long, sectionOrder: Int
    ): List<ExamContentDownloadLink> {
        val exam = examService.getById(examId)
        val examSectionContents =
            examContentRepository.findAllByExamIdAndSectionOrder(exam.id ?: Entity.UNKNOWN_ID, sectionOrder)
        
        return examSectionContents.map {
            ExamContentDownloadLink(
                fileId = it.fileId, link = fileService.generatePublicDownloadLink(it.fileId, 86400)
            )
        }
    }
    
    fun getStudentExamContentDownloadLink(
        examSessionId: Long, sectionOrder: Int
    ): List<ExamContentDownloadLink> {
        val examSession = examSessionRepository.findById(examSessionId).getOrNull() ?: return emptyList()
        if (Auth.userId() != examSession.studentUserId) {
            throw InvalidCredentialException("Exam Session with id: $examSessionId is not belong to your token")
        }
        val exam = examService.getById(examSession.examId)
        val examSectionContents =
            examContentRepository.findAllByExamIdAndSectionOrder(exam.id ?: Entity.UNKNOWN_ID, sectionOrder)
        
        return examSectionContents.map {
            ExamContentDownloadLink(
                fileId = it.fileId, link = fileService.generatePublicDownloadLink(it.fileId, exam.examDuration.toInt())
            )
        }
    }
    
    fun replaceFileIdsWithDownloadLink(string: String): String {
        val pattern = Pattern.compile("<img src=\"([^\"]+)\"/>")
        val matcher = pattern.matcher(string)
        return matcher.replaceAll { matchResult ->
            val imageLink = fileService.generatePublicDownloadLink(matchResult.group(1).toLong(), 24 * 3600)
            "<img src=\"$imageLink\"/>"
        }
    }
}
