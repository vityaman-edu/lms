package ru.itmo.lms.gateway.domain.model

data class Note(
    val id: Id,
    val content: String,
) {
    companion object {
        @JvmInline
        value class Id(val value: Long)
    }
}
