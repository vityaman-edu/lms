package ru.itmo.lms.botalka

import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.utility.MountableFile

class TestContainerInitializer :
    ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Container
    private val postgres = PostgreSQLContainer("postgres")
        .withCopyToContainer(
            MountableFile.forClasspathResource("database/schema.sql"),
            "/docker-entrypoint-initdb.d/init.sql",
        )

    init {
        postgres.start()
    }

    override fun initialize(ctx: ConfigurableApplicationContext) {
        TestPropertyValues.of(
            "spring.r2dbc.url=${postgres.r2dbcUrl()}",
            "spring.r2dbc.username=${postgres.username}",
            "spring.r2dbc.password=${postgres.password}",
        ).applyTo(ctx.environment)
    }

    private fun PostgreSQLContainer<*>.r2dbcUrl(): String {
        val host = this.host
        val port = this.firstMappedPort
        val name = this.databaseName
        return "r2dbc:postgresql://$host:$port/$name"
    }
}
