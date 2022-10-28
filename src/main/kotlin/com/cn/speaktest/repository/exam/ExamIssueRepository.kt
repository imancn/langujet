package com.cn.speaktest.repository.exam

import com.cn.speaktest.model.ExamIssue
import org.springframework.data.mongodb.repository.MongoRepository

interface ExamIssueRepository : MongoRepository<ExamIssue, String>