package com.cn.speaktest.exam.api

import com.cn.speaktest.exam.model.ExamIssue

interface ExamService {
    fun generateExamIssueList(examId: String): List<ExamIssue>
}
