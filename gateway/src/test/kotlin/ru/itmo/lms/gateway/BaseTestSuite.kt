package ru.itmo.lms.gateway

import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.utility.MountableFile

@Suppress("UtilityClassWithPublicConstructor")
abstract class BaseTestSuite {
    companion object {
        @Container
        private val postgres = PostgreSQLContainer("postgres")
            .withCopyToContainer(
                MountableFile.forClasspathResource("database/schema.sql"),
                "/docker-entrypoint-initdb.d/init.sql",
            )

        @JvmStatic
        @BeforeAll
        fun beforeAll() {
            postgres.start()
        }

        @JvmStatic
        @AfterAll
        fun afterAll() {
            postgres.stop()
        }

        @JvmStatic
        @DynamicPropertySource
        fun configureProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.r2dbc.url") { r2dbcUrl() }
            registry.add("spring.r2dbc.username") { postgres.username }
            registry.add("spring.r2dbc.password") { postgres.password }
        }

        private fun r2dbcUrl(): String {
            val host = postgres.host
            val port = postgres.firstMappedPort
            val name = postgres.databaseName
            return "r2dbc:postgresql://$host:$port/$name"
        }
    }
}
