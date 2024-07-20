package com.cn.langujet.domain.correction.service.corrector

import com.cn.langujet.application.advice.LogicalException
import com.cn.langujet.domain.exam.model.ExamType
import com.cn.langujet.domain.exam.model.SectionType

class ScoreCalculator {
    companion object {
        fun calculateAcademicIELTSScore(correctAnswers: Int, sectionType: SectionType): Double {
            return when (sectionType) {
                SectionType.LISTENING -> {
                    when (correctAnswers) {
                        in 39..40 -> 9.0
                        in 37..38 -> 8.5
                        in 35..36 -> 8.0
                        in 33..34 -> 7.5
                        in 30..32 -> 7.0
                        in 27..29 -> 6.5
                        in 23..26 -> 6.0
                        in 19..22 -> 5.5
                        in 15..18 -> 5.0
                        in 13..14 -> 4.5
                        in 10..12 -> 4.0
                        in 8..9 -> 3.5
                        in 6..7 -> 3.0
                        in 4..5 -> 2.5
                        else -> 0.0
                    }
                }
                
                SectionType.READING -> {
                    when (correctAnswers) {
                        in 39..40 -> 9.0
                        in 37..38 -> 8.5
                        in 35..36 -> 8.0
                        in 33..34 -> 7.5
                        in 30..32 -> 7.0
                        in 26..29 -> 6.5
                        in 23..26 -> 6.0
                        in 19..22 -> 5.5
                        in 15..18 -> 5.0
                        in 13..14 -> 4.5
                        in 10..12 -> 4.0
                        in 8..9 -> 3.5
                        in 6..7 -> 3.0
                        in 4..5 -> 2.5
                        in 3..3 -> 2.0
                        else -> 0.0
                    }
                }
                else -> throw LogicalException("There is no score calculation for $sectionType section type")
            }
        }
        
        fun calculateOverAllScore(scores: List<Double>, examType: ExamType): Double {
            return when (examType) {
                ExamType.IELTS_GENERAL, ExamType.IELTS_ACADEMIC -> {
                    scores.sumOf { it } / scores.count()
                }
            }
        }
    }
}
