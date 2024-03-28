package ru.itmo.lms.botalka.api.http.message

import ru.itmo.lms.botalka.api.http.NoteDraftMessage
import ru.itmo.lms.botalka.api.http.NoteMessage
import ru.itmo.lms.botalka.domain.model.Note

fun Note.toMessage(): NoteMessage =
    NoteMessage(
        id = this.id.value,
        content = this.content,
    )

fun NoteDraftMessage.toModel(): Note.Draft =
    Note.Draft(
        content = this.content,
    )
