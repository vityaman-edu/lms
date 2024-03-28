package ru.itmo.lms.gateway.storage.jooq

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.awaitSingle
import org.jooq.DSLContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import ru.itmo.lms.gateway.domain.model.Note
import ru.itmo.lms.gateway.storage.NoteStorage
import ru.itmo.lms.gateway.storage.jooq.entity.toModel
import ru.itmo.lms.gateway.storage.jooq.tables.records.NoteRecord
import ru.itmo.lms.gateway.storage.jooq.tables.references.NOTE

@Repository
class JooqNoteStorage(
    @Autowired private val dsl: DSLContext,
) : NoteStorage {
    override fun getAll(): Flow<Note> =
        dsl.selectFrom(NOTE)
            .toFlux()
            .map { it.toModel() }
            .asFlow()

    override suspend fun create(note: Note.Draft): Note =
        dsl.insertInto(NOTE, NOTE.CONTENT)
            .values(note.content)
            .returningResult(NOTE.ID, NOTE.CONTENT)
            .toMono()
            .map { NoteRecord(it[NOTE.ID]!!, it[NOTE.CONTENT]!!) }
            .map { it.toModel() }
            .awaitSingle()
}
