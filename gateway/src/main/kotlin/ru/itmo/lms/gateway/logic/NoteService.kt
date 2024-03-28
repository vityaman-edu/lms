package ru.itmo.lms.gateway.logic

import kotlinx.coroutines.flow.Flow
import ru.itmo.lms.gateway.domain.model.Note

interface NoteService {
    fun getAll(): Flow<Note>
    suspend fun create(note: Note.Draft): Note
}
