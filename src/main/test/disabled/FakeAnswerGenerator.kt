package disabled

import com.cn.langujet.LangujetApplication
import com.cn.langujet.actor.answer.payload.request.*
import com.cn.langujet.domain.answer.AnswerService
import com.cn.langujet.domain.answer.model.AnswerType
import com.cn.langujet.domain.answer.model.TrueFalseAnswerType
import com.cn.langujet.domain.correction.model.CorrectAnswerEntity
import com.cn.langujet.domain.correction.service.CorrectAnswerService
import com.cn.langujet.domain.exam.service.ExamSessionService
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import kotlin.random.Random

@SpringBootTest(classes = [LangujetApplication::class])
class FakeAnswerGenerator(
    @Autowired
    private val examSessionService: ExamSessionService,
    @Autowired
    private val correctAnswerService: CorrectAnswerService,
    @Autowired
    private val answerService: AnswerService,
) {
    @Test
    fun generate() {
        val examSessionId = ""
        val examSession = examSessionService.getExamSessionById(examSessionId)
        intArrayOf(1, 2).forEach { sectionOrder ->
            val correctAnswers = correctAnswerService.getSectionCorrectAnswers(examSession.examId, sectionOrder)
            answerService.submitBulkAnswers(
                examSessionId,
                sectionOrder,
                correctAnswers.mapNotNull {
                    if (Random.nextInt(1000) < 800) {
                        val random = Random.nextInt(1000) > 300
                        when (it.type) {
                            AnswerType.TEXT -> {
                                val text = (it as CorrectAnswerEntity.CorrectTextAnswerEntity).text
                                TextBulkAnswerRequest(
                                    it.partOrder,
                                    it.questionOrder,
                                    if (random) { text } else "test"
                                )
                            }
                            AnswerType.TEXT_ISSUES -> {
                                val issues = (it as CorrectAnswerEntity.CorrectTextIssuesAnswerEntity).issues.map { it.first() }
                                TextIssuesBulkAnswerRequest(
                                    it.partOrder,
                                    it.questionOrder,
                                    if (random) { issues } else { issues.map { "test" } }
                                )
                            }
                            AnswerType.TRUE_FALSE -> {
                                val issues = (it as CorrectAnswerEntity.CorrectTrueFalseAnswerEntity).issues
                                TrueFalseBulkAnswerRequest(
                                    it.partOrder,
                                    it.questionOrder,
                                    if (random) { issues } else issues.map { TrueFalseAnswerType.entries.random() }
                                )
                            }
                            AnswerType.MULTIPLE_CHOICE -> {
                                val issues = (it as CorrectAnswerEntity.CorrectMultipleChoiceAnswerEntity).issues.map { issue ->
                                    MultipleChoiceIssueAnswerRequest(
                                        issue.order,
                                        if (random) { issue.options } else it.issues.random().options
                                    )
                                }
                                MultipleChoiceBulkAnswerRequest(
                                    it.partOrder,
                                    it.questionOrder,
                                    issues
                                )
                            }
                            AnswerType.VOICE -> TODO()
                        }
                    } else {
                        null
                    }
                }
            )
        }
    }
}