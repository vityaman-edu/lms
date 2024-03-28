package ru.itmo.lms.botalka.domain.model

import ru.itmo.lms.botalka.commons.abbreviated
import java.time.OffsetDateTime

data class Homework(
    val id: Id,
    val title: Title,
    val description: Description,
    val maxScore: Score,
    val publicationMoment: OffsetDateTime,
    val creationMoment: OffsetDateTime,
) {
    @JvmInline
    value class Id(val number: Int) {
        init {
            require(0 < number) {
                """
                    Unique id must be a positive, got $number
                """.trimIndent()
            }
        }
    }

    @JvmInline
    value class Title(val text: String) {
        init {
            require(text.length in lengthRange) {
                """
                    Title must be in range $lengthRange, 
                    got ${text.abbreviated()} with length ${text.length}    
                """.trimIndent()
            }
        }

        companion object {
            private val lengthRange = 8..64
        }
    }

    @JvmInline
    value class Description(val text: String) {
        init {
            require(text.length in lengthRange) {
                """
                    Description must be in range $lengthRange, "
                    got ${text.abbreviated()} with length ${text.length}
                """.trimIndent()
            }
        }

        companion object {
            private val lengthRange = 8..16_384
        }
    }

    @JvmInline
    value class Score(val value: Short) {
        init {
            require(value in range) {
                """
                    Score must be in range $range, got $value
                """.trimIndent()
            }
        }

        companion object {
            private val range = 0..2000
        }
    }

    data class Draft(
        val title: Title,
        val description: Description,
        val maxScore: Score,
        val publicationMoment: OffsetDateTime,
    )
}
