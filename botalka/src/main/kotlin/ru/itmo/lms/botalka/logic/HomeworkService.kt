package ru.itmo.lms.botalka.logic

import ru.itmo.lms.botalka.domain.model.Homework

interface HomeworkService {
    suspend fun create(homework: Homework.Draft): Homework
}
