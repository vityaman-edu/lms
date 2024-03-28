package ru.itmo.lms.botalka.logic

import kotlinx.coroutines.flow.Flow
import ru.itmo.lms.botalka.domain.model.Note

interface NoteService {
    fun getAll(): Flow<Note>
    suspend fun create(note: Note.Draft): Note
}
