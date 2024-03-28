package ru.itmo.lms.botalka.storage.jooq

import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import ru.itmo.lms.botalka.domain.model.Homework
import ru.itmo.lms.botalka.storage.HomeworkStorage
import ru.itmo.lms.botalka.storage.jooq.entity.toModel
import ru.itmo.lms.botalka.storage.jooq.tables.references.HOMEWORK

@Repository
class JooqHomeworkStorage(
    @Autowired private val database: JooqDatabase,
) : HomeworkStorage {
    override suspend fun create(homework: Homework.Draft): Homework =
        database.execute
            .insertInto(
                HOMEWORK,
                HOMEWORK.TITLE,
                HOMEWORK.DESCRIPTION,
                HOMEWORK.MAX_SCORE,
                HOMEWORK.PUBLICATION_MOMENT,
            )
            .values(
                homework.title.text,
                homework.description.text,
                homework.maxScore.value,
                homework.publicationMoment.toLocalDateTime(),
            )
            .returningResult(HOMEWORK.fields().asList())
            .coerce(HOMEWORK)
            .toMono()
            .map { it.toModel() }
            .awaitSingle()
}
