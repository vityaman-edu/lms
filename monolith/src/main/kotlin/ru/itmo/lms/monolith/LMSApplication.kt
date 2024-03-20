package ru.itmo.lms.monolith

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class LMSApplication

fun main(args: Array<String>) {
    runApplication<LMSApplication>(args = args)
}
