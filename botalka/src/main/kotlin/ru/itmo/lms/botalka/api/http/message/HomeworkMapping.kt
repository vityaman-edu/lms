package ru.itmo.lms.botalka.api.http.message

import ru.itmo.lms.botalka.api.http.HomeworkDraftMessage
import ru.itmo.lms.botalka.api.http.HomeworkMessage
import ru.itmo.lms.botalka.domain.model.Homework

fun Homework.toMessage(): HomeworkMessage =
    HomeworkMessage(
        id = this.id.number,
        title = this.title.text,
        description = this.description.text,
        maxScore = this.maxScore.value.toInt(),
        publicationMoment = this.publicationMoment,
        creationMoment = this.creationMoment,
    )

fun Homework.Draft.toMessage(): HomeworkDraftMessage =
    HomeworkDraftMessage(
        title = this.title.text,
        description = this.description.text,
        maxScore = this.maxScore.value.toInt(),
        publicationMoment = this.publicationMoment,
    )

fun HomeworkDraftMessage.toModel(): Homework.Draft =
    Homework.Draft(
        title = Homework.Title(this.title),
        description = Homework.Description(this.description),
        maxScore = Homework.Score(this.maxScore.toShort()),
        publicationMoment = this.publicationMoment,
    )
