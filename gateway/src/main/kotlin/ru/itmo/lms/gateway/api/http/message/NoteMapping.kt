package ru.itmo.lms.gateway.api.http.message

import ru.itmo.lms.gateway.api.http.NoteDraftMessage
import ru.itmo.lms.gateway.api.http.NoteMessage
import ru.itmo.lms.gateway.domain.model.Note

fun Note.toMessage(): NoteMessage =
    NoteMessage(
        id = this.id.value,
        content = this.content,
    )

fun NoteDraftMessage.toModel(): Note.Draft =
    Note.Draft(
        content = this.content,
    )
