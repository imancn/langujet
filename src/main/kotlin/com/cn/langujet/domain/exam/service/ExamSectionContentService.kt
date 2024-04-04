package com.cn.langujet.domain.exam.service

import com.cn.langujet.actor.exam.payload.ExamSectionContentDownloadLink
import com.cn.langujet.actor.util.Auth
import com.cn.langujet.application.advice.FileException
import com.cn.langujet.application.advice.InvalidTokenException
import com.cn.langujet.application.advice.NotFoundException
import com.cn.langujet.application.service.file.domain.data.model.FileBucket
import com.cn.langujet.application.service.file.domain.service.FileService
import com.cn.langujet.domain.exam.model.ExamSectionContent
import com.cn.langujet.domain.exam.repository.ExamSectionContentRepository
import com.cn.langujet.domain.student.service.StudentService
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class ExamSectionContentService(
    private val examSectionContentRepository: ExamSectionContentRepository,
    private val examSessionService: ExamSessionService,
    private val sectionService: SectionService,
    private val examService: ExamService,
    private val studentService: StudentService,
    private val fileService: FileService
    ) {
    fun uploadExamSectionContent(examId: String, sectionOrder: Int, file: MultipartFile): ExamSectionContent {
        sectionService.getSectionsByExamId(examId).find {
            it.order == sectionOrder
        } ?: throw NotFoundException("Section not found")

        val fileEntity = fileService.uploadFile(file, FileBucket.EXAM_CONTENTS)

        return examSectionContentRepository.save(
            ExamSectionContent(
                null,
                examId,
                sectionOrder,
                fileEntity.id ?: throw FileException("Upload Failed")
            )
        )
    }

    fun getAdminExamSectionContentDownloadLink(
        examId: String,
        sectionOrder: Int
    ): List<ExamSectionContentDownloadLink> {
        val exam = examService.getExamById(examId)
        val examSectionContents = examSectionContentRepository.findAllByExamIdAndSectionOrder(exam.id ?: "", sectionOrder)

        return examSectionContents.map {
            ExamSectionContentDownloadLink(
                fileId = it.fileId,
                link = fileService.generatePublicDownloadLink(it.fileId, 86400)
            )
        }
    }

    fun getStudentExamSectionContentDownloadLink(
        examSessionId: String,
        sectionOrder: Int
    ): List<ExamSectionContentDownloadLink> {
        val examSession = examSessionService.getExamSessionById(examSessionId)
        if (studentService.getStudentByUserId(Auth.userId()).id != examSession.studentId) {
            throw InvalidTokenException("Exam Session with id: $examSessionId is not belong to your token")
        }
        val exam = examService.getExamById(examSession.examId)
        val examSectionContents = examSectionContentRepository.findAllByExamIdAndSectionOrder(exam.id ?: "", sectionOrder)

        return examSectionContents.map {
            ExamSectionContentDownloadLink(
                fileId = it.fileId,
                link = fileService.generatePublicDownloadLink(it.fileId, exam.examDuration.toInt())
            )
        }
    }
}
