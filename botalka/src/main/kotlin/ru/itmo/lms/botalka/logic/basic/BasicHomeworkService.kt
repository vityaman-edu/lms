package ru.itmo.lms.botalka.logic.basic

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.itmo.lms.botalka.domain.model.Homework
import ru.itmo.lms.botalka.logic.HomeworkService
import ru.itmo.lms.botalka.storage.HomeworkStorage

@Service
class BasicHomeworkService(
    @Autowired private val storage: HomeworkStorage,
) : HomeworkService {
    override suspend fun create(homework: Homework.Draft): Homework =
        storage.create(homework)
}
