package ru.itmo.lms.gateway.api.http.endpoint

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import ru.itmo.lms.gateway.api.http.apis.MonitoringApi

@RestController
class MonitoringHttpApi : MonitoringApi {
    override suspend fun monitoringPingGet(): ResponseEntity<String> =
        ResponseEntity.ok("pong")
}
