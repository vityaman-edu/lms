package ru.itmo.lms.gateway.logic.basic

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.springframework.stereotype.Service
import ru.itmo.lms.gateway.domain.model.Note
import ru.itmo.lms.gateway.logic.NoteService

@Service
class BasicNoteService : NoteService {
    override fun getAll(): Flow<Note> = flow {
        for (note in listOf(
            Note(Note.Id(1), "Note with id 1"),
            Note(Note.Id(2), "Note with id 2"),
        )) {
            emit(note)
        }
    }

    override suspend fun create(note: Note.Draft): Note =
        Note(Note.Id(1), "Created note with id 1")
}
