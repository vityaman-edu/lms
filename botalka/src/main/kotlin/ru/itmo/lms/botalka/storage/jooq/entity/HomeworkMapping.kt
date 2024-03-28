package ru.itmo.lms.botalka.storage.jooq.entity

import ru.itmo.lms.botalka.config.Config
import ru.itmo.lms.botalka.domain.model.Homework
import ru.itmo.lms.botalka.storage.jooq.tables.records.HomeworkRecord

fun HomeworkRecord.toModel(): Homework =
    Homework(
        id = Homework.Id(this.id!!),
        title = Homework.Title(this.title),
        description = Homework.Description(this.description),
        maxScore = Homework.Score(this.maxScore),
        publicationMoment = this.publicationMoment.atOffset(Config.zoneOffset),
        creationMoment = this.creationMoment!!.atOffset(Config.zoneOffset),
    )
