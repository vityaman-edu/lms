package ru.itmo.lms.botalka.storage.jooq

import org.jooq.DSLContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
data class JooqDatabase(@Autowired val execute: DSLContext)
