import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektCreateBaselineTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.openapitools.generator.gradle.plugin.tasks.GenerateTask as OpenAPIGenerateTask

plugins {
    kotlin("jvm") version "1.9.22"

    kotlin("plugin.spring") version "1.9.22"
    id("org.springframework.boot") version "3.2.3"
    id("io.spring.dependency-management") version "1.1.4"

    id("org.openapi.generator") version "5.3.0"

    id("org.jlleitschuh.gradle.ktlint") version "12.1.0"
    id("io.gitlab.arturbosch.detekt") version "1.23.5"

    id("org.jetbrains.kotlinx.kover") version "0.7.6"

    id("org.jooq.jooq-codegen-gradle") version "3.19.6"
}

group = "ru.itmo"
version = "0.0.1"

val jvmTarget = "21"
val basePackage = "$group.lms.gateway"

val jooqVersion = "3.19.6"
val testcontainersVersion = "1.19.7"

java {
    sourceCompatibility = JavaVersion.VERSION_21
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    developmentOnly("org.springframework.boot:spring-boot-devtools")

    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")

    implementation("org.springdoc:springdoc-openapi-starter-webflux-ui:2.4.0")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    // implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
    // runtimeOnly("org.postgresql:r2dbc-postgresql")

    implementation("org.jooq:jooq:$jooqVersion")
    implementation("org.jooq:jooq-kotlin:$jooqVersion")
    jooqCodegen("jakarta.xml.bind:jakarta.xml.bind-api:4.0.2")
    jooqCodegen("org.jooq:jooq-meta-extensions:$jooqVersion")
    jooqCodegen("org.jooq:jooq-meta-kotlin:$jooqVersion")
    jooqCodegen("org.postgresql:postgresql:42.7.2")
    jooqCodegen("org.testcontainers:postgresql:$testcontainersVersion")
    jooqCodegen("org.testcontainers:testcontainers:$testcontainersVersion")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
    testImplementation(kotlin("test"))

    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.23.3")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = jvmTarget
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

val generatedDir = "$projectDir/build/generated"
val generateControllers = "generateControllers"

tasks.register<OpenAPIGenerateTask>(generateControllers) {
    val spec = "$projectDir/src/main/resources/static/openapi/api.yml"
    val pkg = "$basePackage.api.http"

    group = "openapi tools"
    description = "Generates code from an Open API specification"
    verbose = false
    generatorName = "kotlin-spring"
    inputSpec = spec
    outputDir = "$generatedDir/openapi"
    packageName = pkg
    modelPackage = pkg
    generateModelTests = false
    generateApiTests = false
    configOptions =
        mapOf(
            "serializationLibrary" to "jackson",
            "enumPropertyNaming" to "UPPERCASE",
            "dateLibrary" to "java8",
            "bigDecimalAsString" to "true",
            "hideGenerationTimestamp" to "true",
            "useBeanValidation" to "false",
            "performBeanValidation" to "false",
            "openApiNullable" to "false",
            "reactive" to "true",
            "interfaceOnly" to "true",
        )
}

tasks.compileKotlin.configure {
    dependsOn(tasks.getByName(generateControllers))
}

sourceSets {
    main {
        java {
            srcDir("$generatedDir/openapi/src/main/kotlin")
            srcDir("$generatedDir/jooq/src/main/kotlin")
        }
    }
}

ktlint {
    filter {
        exclude {
            val directories = listOf("generated")
            val path = it.file.path
            directories
                .flatMap { dir -> listOf("\\$dir\\", "/$dir/") }
                .any { fragment -> path.contains(fragment) }
        }
    }
}

tasks.runKtlintCheckOverMainSourceSet {
    dependsOn(tasks.getByName(generateControllers))
}

detekt {
    buildUponDefaultConfig = true
    allRules = false
    config.setFrom(file("$projectDir/../config/detekt.yml"))
}

tasks.withType<Detekt>().configureEach {
    reports {
        html.required = true
        txt.required = true
        sarif.required = true
        md.required = true
    }
}

tasks.withType<Detekt>().configureEach {
    jvmTarget = jvmTarget
}

tasks.withType<DetektCreateBaselineTask>().configureEach {
    jvmTarget = jvmTarget
}

koverReport {
    filters {
        excludes {
            classes(
                "$basePackage.api.http.apis.*",
                "$basePackage.GatewayApplicationKt",
            )
        }
    }
    verify {
        rule {
            isEnabled = true
            bound { minValue = 80 }
        }
    }
}

jooq {
    val schemaSql = "src/main/resources/database/schema.sql"

    version = jooqVersion
    executions {
        create("main") {
            configuration {
                logging = org.jooq.meta.jaxb.Logging.DEBUG
                jdbc {
                    driver = "org.testcontainers.jdbc.ContainerDatabaseDriver"
                    url = "jdbc:tc:postgresql:16:///test?TC_TMPFS=/testtmpfs:rw&amp;TC_INITSCRIPT=file:$schemaSql"
                    username = "postgres"
                    password = "postgres"
                }
                generator {
                    name = "org.jooq.codegen.KotlinGenerator"
                    database {
                        name = "org.jooq.meta.postgres.PostgresDatabase"
                        inputSchema = "lms"
                        includes = ".*"
                    }
                    generate {
                        isPojosAsKotlinDataClasses = true
                        isImplicitJoinPathsToMany = false
                    }
                    target {
                        packageName = "$basePackage.storage.jooq"
                        directory = "$generatedDir/jooq/main"
                    }
                }
            }
        }
    }
}
