package ru.itmo.lms.botalka.logic.basic

import kotlinx.coroutines.flow.Flow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.itmo.lms.botalka.domain.model.Note
import ru.itmo.lms.botalka.logic.NoteService
import ru.itmo.lms.botalka.storage.NoteStorage

@Service
class BasicNoteService(
    @Autowired private val storage: NoteStorage,
) : NoteService {
    override fun getAll(): Flow<Note> =
        storage.getAll()

    override suspend fun create(note: Note.Draft): Note =
        storage.create(note)
}
