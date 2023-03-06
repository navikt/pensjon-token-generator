package no.nav.pensjon

import com.nimbusds.jose.jwk.RSAKey
import no.nav.pensjonsamhandling.maskinporten.client.MaskinportenClient
import no.nav.pensjonsamhandling.maskinporten.client.MaskinportenConfig
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
class ClientConfig(
    @Value("#{'\${MASKINPORTEN_SCOPES}'.split(' ')}")
    val scopes: List<String>,

    @Value("\${MASKINPORTEN_CLIENT_ID}")
    clientId: String,

    @Value("\${MASKINPORTEN_CLIENT_JWK}")
    key: String
) {
    private val config = MaskinportenConfig(
        "https://test.maskinporten.no",
        clientId,
        RSAKey.parse(key),
        0
    )
    val client = MaskinportenClient(config)
}
