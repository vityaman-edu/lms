package ru.itmo.lms.gateway.storage

import kotlinx.coroutines.flow.Flow
import ru.itmo.lms.gateway.domain.model.Note

interface NoteStorage {
    fun getAll(): Flow<Note>
    suspend fun create(note: Note.Draft): Note
}
