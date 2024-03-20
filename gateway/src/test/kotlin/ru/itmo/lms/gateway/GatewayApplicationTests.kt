package ru.itmo.lms.gateway

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import ru.itmo.lms.gateway.api.http.apis.MonitoringApi
import kotlin.test.assertEquals

@SpringBootTest
class GatewayApplicationTests(
    @Autowired val monitoringApi: MonitoringApi,
) {
    @Test
    fun contextLoads() {
        // Okay
    }

    @Test
    fun pingSucceeds() {
        val body = runBlocking { monitoringApi.monitoringPingGet().body }
        assertEquals(body, "pong")
    }
}
