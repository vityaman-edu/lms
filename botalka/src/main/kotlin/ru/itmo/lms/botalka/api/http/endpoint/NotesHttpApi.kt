package ru.itmo.lms.botalka.api.http.endpoint

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import ru.itmo.lms.botalka.api.http.NoteDraftMessage
import ru.itmo.lms.botalka.api.http.NoteMessage
import ru.itmo.lms.botalka.api.http.apis.NotesApi
import ru.itmo.lms.botalka.api.http.message.toMessage
import ru.itmo.lms.botalka.api.http.message.toModel
import ru.itmo.lms.botalka.logic.NoteService

@RestController
class NotesHttpApi(
    @Autowired private val noteService: NoteService,
) : NotesApi {
    override fun notesGet(): ResponseEntity<Flow<NoteMessage>> =
        ResponseEntity.ok(noteService.getAll().map { it.toMessage() })

    override suspend fun notesPost(
        noteDraftMessage: NoteDraftMessage,
    ): ResponseEntity<NoteMessage> {
        val noteDraft = noteDraftMessage.toModel()
        val note = noteService.create(noteDraft)
        return ResponseEntity.ok(note.toMessage())
    }
}
