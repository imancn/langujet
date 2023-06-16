package com.cn.langujet.domain.exam.service

import com.cn.langujet.actor.exam.payload.dto.SuggestionDto
import com.cn.langujet.domain.exam.model.*
import com.cn.langujet.domain.professor.Professor

interface ExamSessionServiceInterface {

    fun getStudentExamSessionWithAuthToken(authToken: String, examSessionId: String): ExamSession
    fun getProfessorExamSessionWithAuthToken(authToken: String, examSessionId: String): ExamSession
    fun enrollExamSession(examRequest: ExamRequest, professor: Professor, exam: Exam): ExamSession
    fun startExamSession(authToken: String, examSessionId: String): ExamIssue
    fun nextExamIssue(authToken: String, examSessionId: String, currentExamIssueOrder: Int): ExamIssue
    fun finishExamSession(authToken: String, examSessionId: String): ExamSession
    fun rateExamSession(authToken: String, examSessionId: String, suggestion: SuggestionDto): ExamSession
    fun isExamIssueAvailable(examIssueId: String): Boolean
    fun getExamSessionBySuggestionId(suggestionId: String): ExamSession
}