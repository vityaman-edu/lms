package ru.itmo.lms.botalka.domain.model

data class Note(
    val id: Id,
    val content: String,
) {
    @JvmInline
    value class Id(val value: Long)

    data class Draft(val content: String)
}
