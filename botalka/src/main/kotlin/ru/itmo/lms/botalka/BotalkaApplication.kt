package ru.itmo.lms.botalka

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jooq.JooqAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication(exclude = [JooqAutoConfiguration::class])
class BotalkaApplication

fun main(args: Array<String>) {
    runApplication<BotalkaApplication>(args = args)
}
