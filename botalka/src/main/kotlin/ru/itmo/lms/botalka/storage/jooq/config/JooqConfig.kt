package ru.itmo.lms.botalka.storage.jooq.config

import io.r2dbc.spi.ConnectionFactory
import org.jooq.DSLContext
import org.jooq.SQLDialect
import org.jooq.impl.DSL
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.r2dbc.connection.TransactionAwareConnectionFactoryProxy

@Configuration
class JooqConfig {
    @Bean
    fun dslContext(connectionFactory: ConnectionFactory): DSLContext {
        val factory = TransactionAwareConnectionFactoryProxy(connectionFactory)
        val dialect = SQLDialect.POSTGRES
        return DSL.using(factory, dialect)
    }
}
