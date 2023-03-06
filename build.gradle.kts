plugins {
    kotlin("jvm") version "1.6.10"
    kotlin("plugin.spring") version "1.6.10"
    id("org.springframework.boot") version "2.6.4"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
}

group = "no.nav.pensjon-saksbehandling"
version = "1.0"
description = "pensjon-maskinporten-test"

repositories {
    mavenCentral()
    maven("https://maven.pkg.github.com/navikt/maskinporten-client") {
        credentials {
            username = "token"
            password = System.getenv("GITHUB_TOKEN")
        }
    }
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("com.nimbusds", "nimbus-jose-jwt", "9.21")
    implementation("io.micrometer", "micrometer-registry-prometheus", "1.8.3")
    implementation("no.nav.pensjonsamhandling", "maskinporten-client", "1.0.1")
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
