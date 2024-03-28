package ru.itmo.lms.gateway.storage.jooq

import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import org.jooq.Publisher as JooqPublisher
import org.reactivestreams.Publisher as ReactivePublisher

@Suppress("UNCHECKED_CAST") // IDEA fails to infer the type
private fun <T> publisher(jooq: JooqPublisher<T>) =
    jooq as ReactivePublisher<T>

fun <T> JooqPublisher<T>.toFlux(): Flux<T> =
    Flux.from(publisher(this))

fun <T> JooqPublisher<T>.toMono(): Mono<T> =
    Mono.from(publisher(this))
