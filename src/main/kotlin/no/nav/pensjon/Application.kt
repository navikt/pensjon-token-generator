package no.nav.pensjon

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer
import org.springframework.web.client.RestTemplate

@SpringBootApplication
class Application {
    @Bean
    fun propertySourcesPlaceholderConfigurer() = PropertySourcesPlaceholderConfigurer()
    @Bean
    fun maskinportenRestTemplate() = RestTemplate()
}

fun main(vararg args: String) {
    runApplication<Application>(*args)
}
