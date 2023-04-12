package com.cn.speaktest.exam.service

import com.cn.speaktest.exam.model.Suggestion
import com.cn.speaktest.exam.model.Exam
import com.cn.speaktest.exam.model.ExamIssue
import com.cn.speaktest.exam.model.ExamRequest
import com.cn.speaktest.exam.model.ExamSession
import com.cn.speaktest.professor.Professor

interface ExamSessionServiceInterface {

    fun getStudentExamSessionWithAuthToken(authToken: String, examSessionId: String): ExamSession
    fun getProfessorExamSessionWithAuthToken(authToken: String, examSessionId: String): ExamSession
    fun enrollExamSession(examRequest: ExamRequest, professor: Professor, exam: Exam): ExamSession
    fun startExamSession(authToken: String, examSessionId: String): ExamIssue
    fun nextExamIssue(authToken: String, examSessionId: String, currentExamIssueOrder: Int): ExamIssue
    fun finishExamSession(authToken: String, examSessionId: String): ExamSession
    fun rateExamSession(authToken: String, examSessionId: String, score: Double, suggestion: Suggestion?): ExamSession
    fun isExamIssueAvailable(examIssueId: String): Boolean
    fun getExamSessionBySuggestionId(suggestionId: String): ExamSession
}