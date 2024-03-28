package ru.itmo.lms.gateway

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jooq.JooqAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication(
    exclude = [
        JooqAutoConfiguration::class,
    ],
)
class GatewayApplication

fun main(args: Array<String>) {
    runApplication<GatewayApplication>(args = args)
}
