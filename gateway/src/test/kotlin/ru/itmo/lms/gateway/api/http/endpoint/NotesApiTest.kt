package ru.itmo.lms.gateway.api.http.endpoint

import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import ru.itmo.lms.gateway.api.http.NoteDraftMessage
import ru.itmo.lms.gateway.api.http.NoteMessage
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

@SpringBootTest
class NotesApiTest(@Autowired private val api: NotesHttpApi) {
    @Test
    fun notesGetSmoke() {
        val notes = mutableListOf<NoteMessage>()
        runBlocking { api.notesGet().body?.toList(notes) }
        assertContentEquals(
            notes,
            listOf(
                NoteMessage(1, "Note with id 1"),
                NoteMessage(2, "Note with id 2"),
            ),
        )
    }

    @Test
    fun notesPostSmoke() {
        val draft = NoteDraftMessage("Created note with id 1")
        val note = runBlocking { api.notesPost(draft).body }
        assertEquals(draft.content, note?.content)
    }
}
