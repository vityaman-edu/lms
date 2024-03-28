package ru.itmo.lms.botalka.api.http.endpoint

import io.kotest.matchers.collections.shouldBeUnique
import io.kotest.matchers.date.shouldBeAfter
import io.kotest.matchers.date.shouldHaveSameInstantAs
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import ru.itmo.lms.botalka.TestContainerInitializer
import ru.itmo.lms.botalka.api.http.apis.HomeworkApi
import ru.itmo.lms.botalka.api.http.message.toMessage
import ru.itmo.lms.botalka.domain.model.Homework
import java.time.OffsetDateTime

@SpringBootTest
@ContextConfiguration(initializers = [TestContainerInitializer::class])
class HomeworkApiTest(
    @Autowired val api: HomeworkApi,
) {
    private val drafts = listOf(
        Homework.Draft(
            Homework.Title("Test Homework 1"),
            Homework.Description("Test Homework 1 Description"),
            Homework.Score(100),
            OffsetDateTime.parse("2024-04-01T10:15:30+03:00"),
        ),
        Homework.Draft(
            Homework.Title("Test Homework 2"),
            Homework.Description("Test Homework 2 Description"),
            Homework.Score(250),
            OffsetDateTime.parse("2024-05-01T12:00:30+03:00"),
        ),
        Homework.Draft(
            Homework.Title("Test Homework 3"),
            Homework.Description("Test Homework 3 Description"),
            Homework.Score(100),
            OffsetDateTime.parse("2024-05-02T12:00:30+03:00"),
        ),
        Homework.Draft(
            Homework.Title("Test Homework 4"),
            Homework.Description("Test Homework 4 Description"),
            Homework.Score(300),
            OffsetDateTime.parse("2024-05-02T12:00:30+03:00"),
        ),
    )

    @Test
    fun createHomework() {
        val startingPoint = OffsetDateTime.now()

        val draftToResultList = runBlocking {
            drafts.map { draft ->
                val message = draft.toMessage()
                val result = async { api.homeworkPost(message).body }
                Pair(draft, result)
            }.map { (draft, result) ->
                Pair(draft, result.await() ?: throw NullPointerException())
            }
        }

        draftToResultList.forEach { (l, r) ->
            l.title.text shouldBe r.title
            l.description.text shouldBe r.description
            l.maxScore.value shouldBe r.maxScore
            l.publicationMoment shouldHaveSameInstantAs r.publicationMoment
        }

        val results = draftToResultList.map { it.second }
        results.forEach {
            it.creationMoment shouldBeAfter startingPoint
        }

        val ids = results.map { it.id }
        ids.shouldBeUnique()
    }
}
