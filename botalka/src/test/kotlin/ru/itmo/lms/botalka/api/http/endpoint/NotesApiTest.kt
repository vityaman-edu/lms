package ru.itmo.lms.botalka.api.http.endpoint

import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import ru.itmo.lms.botalka.TestContainerInitializer
import ru.itmo.lms.botalka.api.http.NoteDraftMessage
import ru.itmo.lms.botalka.api.http.NoteMessage
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

@SpringBootTest
@ContextConfiguration(initializers = [TestContainerInitializer::class])
@TestMethodOrder(OrderAnnotation::class)
class NotesApiTest(@Autowired private val api: NotesHttpApi) {
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
