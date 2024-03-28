package ru.itmo.lms.botalka.api.http.endpoint

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import kotlin.test.assertEquals

@SpringBootTest
class MonitoringApiTest(@Autowired val api: MonitoringHttpApi) {
    @Test
    fun pingSucceeds() {
        val body = runBlocking { api.monitoringPingGet().body }
        assertEquals(body, "pong")
    }
}
