package ru.itmo.lms.gateway.api.http.endpoint

import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.*
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.testcontainers.junit.jupiter.Testcontainers
import ru.itmo.lms.gateway.BaseTestSuite
import ru.itmo.lms.gateway.api.http.NoteDraftMessage
import ru.itmo.lms.gateway.api.http.NoteMessage
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

@SpringBootTest
@TestMethodOrder(OrderAnnotation::class)
class NotesApiTest(@Autowired private val api: NotesHttpApi) : BaseTestSuite() {
    private val contents = listOf(
        "Example note content",
        "It is just a text, nothing more",
        "Smoke testing is okay...",
        "Да мне было страшно, но я это сделал",
    )

    @Test
    @Order(1)
    fun notesPostSmoke() {
        contents.forEach { content ->
            val message = NoteDraftMessage(content)
            val note = runBlocking { api.notesPost(message).body }
            assertEquals(content, note?.content)
        }
    }

    @Test
    @Order(2)
    fun notesGetSmoke() {
        val notes = mutableListOf<NoteMessage>()
        runBlocking { api.notesGet().body?.toList(notes) }
        assertContentEquals(contents, notes.map { it.content })
    }
}
