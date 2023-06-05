package com.cn.speaktest.domain.exam.service

import com.cn.speaktest.domain.exam.model.*
import com.cn.speaktest.domain.professor.Professor

interface ExamSessionServiceInterface {

    fun getStudentExamSessionWithAuthToken(authToken: String, examSessionId: String): ExamSession
    fun getProfessorExamSessionWithAuthToken(authToken: String, examSessionId: String): ExamSession
    fun enrollExamSession(examRequest: ExamRequest, professor: Professor, examMeta: ExamMeta): ExamSession
    fun startExamSession(authToken: String, examSessionId: String): ExamIssue
    fun nextExamIssue(authToken: String, examSessionId: String, currentExamIssueOrder: Int): ExamIssue
    fun finishExamSession(authToken: String, examSessionId: String): ExamSession
    fun rateExamSession(authToken: String, examSessionId: String, score: Double, suggestion: Suggestion?): ExamSession
    fun isExamIssueAvailable(examIssueId: String): Boolean
    fun getExamSessionBySuggestionId(suggestionId: String): ExamSession
}