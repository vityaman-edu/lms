package ru.itmo.lms.botalka.storage

import kotlinx.coroutines.flow.Flow
import ru.itmo.lms.botalka.domain.model.Note

interface NoteStorage {
    fun getAll(): Flow<Note>
    suspend fun create(note: Note.Draft): Note
}
