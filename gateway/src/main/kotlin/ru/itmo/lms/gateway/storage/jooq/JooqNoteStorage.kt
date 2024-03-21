package ru.itmo.lms.gateway.storage.jooq

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.awaitSingle
import org.jooq.DSLContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import ru.itmo.lms.gateway.domain.model.Note
import ru.itmo.lms.gateway.storage.NoteStorage
import ru.itmo.lms.gateway.storage.jooq.entity.toModel
import ru.itmo.lms.gateway.storage.jooq.tables.records.NoteRecord
import ru.itmo.lms.gateway.storage.jooq.tables.references.NOTE

@Repository
class JooqNoteStorage(
    @Autowired private val dsl: DSLContext,
) : NoteStorage {
    override fun getAll(): Flow<Note> {
        val sql = dsl
            .selectFrom(NOTE)

        return Flux.from(sql)
            .map { it.toModel() }
            .asFlow()
    }

    override suspend fun create(note: Note.Draft): Note {
        val sql = dsl
            .insertInto(NOTE, NOTE.CONTENT)
            .values(note.content)
            .returningResult(NOTE.ID, NOTE.CONTENT)

        return Mono.from(sql)
            .map { row -> NoteRecord(row.value1()!!, row.value2()!!) }
            .map { it.toModel() }
            .awaitSingle()
    }
}
