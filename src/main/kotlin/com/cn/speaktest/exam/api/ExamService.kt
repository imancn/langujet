package com.cn.speaktest.exam.api

import com.cn.speaktest.exam.model.Exam
import com.cn.speaktest.exam.model.ExamIssue
import com.cn.speaktest.exam.model.ExamRequest
import com.cn.speaktest.professor.Professor

interface ExamService {
    fun generateExamIssueList(examId: String): List<ExamIssue>
    fun submitExam(examRequest: ExamRequest, professor: Professor): Exam
}
