package no.nav.pensjon

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MaskinportenTest {
    companion object {
        @JvmStatic
        fun main(vararg args: String) {
            runApplication<MaskinportenTest>(*args)
        }
    }
}