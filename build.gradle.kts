plugins {
    kotlin("jvm") version "2.2.10"
    kotlin("plugin.spring") version "2.2.10"
    id("org.springframework.boot") version "3.5.5"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "no.nav.pensjon-saksbehandling"
version = "1.0"
description = "pensjon-maskinporten-test"

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.micrometer:micrometer-registry-prometheus:1.13.4")
    implementation("net.logstash.logback:logstash-logback-encoder:8.1")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-web")
}

tasks.jar {
    manifest {
        attributes("Main-Class" to "Application.AppKt")
    }
}