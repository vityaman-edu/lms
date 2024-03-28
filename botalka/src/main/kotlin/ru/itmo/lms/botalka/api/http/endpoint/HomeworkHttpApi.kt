package ru.itmo.lms.botalka.api.http.endpoint

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import ru.itmo.lms.botalka.api.http.HomeworkDraftMessage
import ru.itmo.lms.botalka.api.http.HomeworkMessage
import ru.itmo.lms.botalka.api.http.apis.HomeworkApi
import ru.itmo.lms.botalka.api.http.message.toMessage
import ru.itmo.lms.botalka.api.http.message.toModel
import ru.itmo.lms.botalka.logic.HomeworkService

@RestController
class HomeworkHttpApi(
    @Autowired private val service: HomeworkService,
) : HomeworkApi {
    override suspend fun homeworkPost(
        homeworkDraftMessage: HomeworkDraftMessage,
    ): ResponseEntity<HomeworkMessage> {
        val draft = homeworkDraftMessage.toModel()
        val homework = service.create(draft)
        return ResponseEntity.ok(homework.toMessage())
    }
}
