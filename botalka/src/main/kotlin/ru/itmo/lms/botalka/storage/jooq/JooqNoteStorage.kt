package ru.itmo.lms.botalka.storage.jooq

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.awaitSingle
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import ru.itmo.lms.botalka.domain.model.Note
import ru.itmo.lms.botalka.storage.NoteStorage
import ru.itmo.lms.botalka.storage.jooq.entity.toModel
import ru.itmo.lms.botalka.storage.jooq.tables.records.NoteRecord
import ru.itmo.lms.botalka.storage.jooq.tables.references.NOTE

@Repository
class JooqNoteStorage(
    @Autowired private val database: JooqDatabase,
) : NoteStorage {
    override fun getAll(): Flow<Note> =
        database.execute
            .selectFrom(NOTE)
            .toFlux()
            .map { it.toModel() }
            .asFlow()

    override suspend fun create(note: Note.Draft): Note =
        database.execute
            .insertInto(NOTE, NOTE.CONTENT)
            .values(note.content)
            .returningResult(NOTE.ID, NOTE.CONTENT)
            .toMono()
            .map { NoteRecord(it[NOTE.ID]!!, it[NOTE.CONTENT]!!) }
            .map { it.toModel() }
            .awaitSingle()
}
