plugins {
    kotlin("jvm") version "2.0.0"
    kotlin("plugin.spring") version "1.8.21"
    id("org.springframework.boot") version "3.3.2"
    id("io.spring.dependency-management") version "1.1.6"
}

group = "no.nav.pensjon-saksbehandling"
version = "1.0"
description = "pensjon-maskinporten-test"


repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("com.nimbusds", "nimbus-jose-jwt", "9.21")
    implementation("io.micrometer:micrometer-registry-prometheus")
    implementation("net.logstash.logback", "logstash-logback-encoder", "7.3")
    implementation("org.springframework.boot", "spring-boot-starter-actuator")
    implementation("org.springframework.boot", "spring-boot-starter-oauth2-client")
    implementation("org.springframework.boot", "spring-boot-starter-security")
    implementation("org.springframework.boot", "spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot", "spring-boot-starter-web")
    testImplementation(kotlin("test-junit5"))
    testImplementation("org.springframework.boot", "spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
        exclude(module = "mockito-core")
    }
    testImplementation("org.springframework.security", "spring-security-test")
}


tasks.jar{
    manifest {
        attributes("Main-Class" to "Application.AppKt")
    }
}
