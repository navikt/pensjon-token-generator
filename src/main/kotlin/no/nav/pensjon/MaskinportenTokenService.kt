package no.nav.pensjon

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.nimbusds.jose.JOSEObjectType
import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.JWSHeader
import com.nimbusds.jose.crypto.RSASSASigner
import com.nimbusds.jose.jwk.RSAKey
import com.nimbusds.jwt.JWTClaimsSet
import com.nimbusds.jwt.SignedJWT
import net.logstash.logback.marker.Markers.append
import org.slf4j.LoggerFactory.getLogger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod.POST
import org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.*
import java.time.Instant
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalAmount
import java.util.*

@Service
class MaskinportenTokenService(
    private val restTemplate: RestTemplate,
    private val clientId: String,
    private val rsaKey: RSAKey,
    private val issuer: String,
    private val tokenEndpoint: String,
) {
    private val logger = getLogger(javaClass)

    private val signer: RSASSASigner = RSASSASigner(rsaKey.toPrivateKey())

    @Autowired
    constructor(
        maskinportenRestTemplate: RestTemplate,
        @Value("\${MASKINPORTEN_CLIENT_ID}") clientId: String,
        @Value("\${MASKINPORTEN_CLIENT_JWK}") clientJwk: String,
        @Value("\${MASKINPORTEN_ISSUER}") issuer: String,
        @Value("\${MASKINPORTEN_TOKEN_ENDPOINT}") tokenEndpoint: String,
    ) : this(maskinportenRestTemplate, clientId, RSAKey.parse(clientJwk), issuer, tokenEndpoint)

    fun accessToken(scope: List<String>, audience: List<String>, pid: String?) = fetch(scope, audience, pid).accessToken

    fun fetch(
        scope: List<String>,
        audience: List<String>,
        pid: String?,
    ): ClientCredentialsTokenResponse {
        val assertion = createAssertion(scope, audience, pid)
        return fetch(assertion)
    }

    private fun fetch(assertion: String): ClientCredentialsTokenResponse = try {
        restTemplate.exchange<ClientCredentialsTokenResponse>(
            tokenEndpoint,
            POST,
            HttpEntity<MultiValueMap<String, String>>(
                LinkedMultiValueMap<String, String>().apply {
                    add("grant_type", "urn:ietf:params:oauth:grant-type:jwt-bearer")
                    add("assertion", assertion)
                },
                org.springframework.http.HttpHeaders().apply {
                    contentType = APPLICATION_FORM_URLENCODED
                }
            ),
        ).body
    } catch (e: HttpStatusCodeException) {
        logger.error(
            append("error_response", e.responseBodyAsString),
            "Failed to fetch token, got status={}, message={}", e.statusText, e.message,
        )
        throw RuntimeException("Unable to fetch token", e)
    } ?: throw RuntimeException("Received empty body in response")

    private fun createAssertion(scope: List<String>, audience: List<String>, pid: String?, issueTime: Instant = Instant.now()): String =
        SignedJWT(
            JWSHeader.Builder(JWSAlgorithm.RS256)
                .keyID(rsaKey.keyID)
                .type(JOSEObjectType.JWT)
                .build(),

            JWTClaimsSet.Builder()
                .issuer(clientId)
                .audience(issuer)
                .issueTime(Date.from(issueTime))
                .claim("scope", scope)
                .expirationTime(Date.from(issueTime.plus(60, ChronoUnit.SECONDS)))
                .jwtID(UUID.randomUUID().toString())
                .apply {
                    if (audience.isNotEmpty()) {
                        claim("resource", audience)
                    }
                    if (pid != null) {
                        claim("pid", pid)
                    }
                }
                .build()
        ).apply { sign(signer) }
            .serialize()


    data class ClientCredentialsTokenResponse(
        @JsonProperty("token_type") val tokenType: String,
        @JsonProperty("expires_in") val expiresIn: Long,
        @JsonProperty("access_token") val accessToken: String,
    ) {
        @JsonIgnore
        val issued = LocalDateTime.now()

        @JsonIgnore
        fun expires(expireRestriction: TemporalAmount): LocalDateTime {
            return issued.plusSeconds(expiresIn).minus(expireRestriction)
        }
    }
}
