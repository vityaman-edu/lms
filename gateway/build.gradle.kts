import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.openapitools.generator.gradle.plugin.tasks.GenerateTask

plugins {
    id("org.springframework.boot") version "3.2.3"
    id("io.spring.dependency-management") version "1.1.4"
    id("org.openapi.generator") version "5.3.0"
    id("org.jlleitschuh.gradle.ktlint") version "12.1.0"
    kotlin("jvm") version "1.9.22"
    kotlin("plugin.spring") version "1.9.22"
}

group = "ru.itmo"
version = "0.0.1"

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
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springdoc:springdoc-openapi-starter-webflux-ui:2.4.0")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "21"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

val generatedDir = "${layout.projectDirectory}/build/generated"
val generateControllers = "generateControllers"

tasks.register<GenerateTask>(generateControllers) {
    val spec = "${layout.projectDirectory}/src/main/resources/static/openapi/api.yml"
    val pkg = "ru.itmo.lms.gateway.api.http"

    group = "openapi tools"
    description = "Generates code from an Open API specification"
    verbose = false
    generatorName = "kotlin-spring"
    inputSpec = spec
    outputDir = generatedDir
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
            srcDir("$generatedDir/src/main/kotlin")
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
