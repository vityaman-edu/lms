package ru.itmo.lms.gateway.storage.jooq.entity

import ru.itmo.lms.gateway.domain.model.Note
import ru.itmo.lms.gateway.storage.jooq.tables.records.NoteRecord

fun NoteRecord.toModel(): Note =
    Note(
        id = Note.Id(this.id!!),
        content = this.content,
    )

fun Note.Draft.toRecord(): NoteRecord =
    NoteRecord(
        content = this.content,
    )
