package ru.itmo.lms.gateway.api.http

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/monitoring")
class MonitoringHttpApi {
    @GetMapping("/ping")
    suspend fun ping(): ResponseEntity<String> {
        return ResponseEntity.ok("pong")
    }
}