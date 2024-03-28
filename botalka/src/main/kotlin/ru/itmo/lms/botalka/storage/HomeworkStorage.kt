package ru.itmo.lms.botalka.storage

import ru.itmo.lms.botalka.domain.model.Homework

interface HomeworkStorage {
    suspend fun create(homework: Homework.Draft): Homework
}
