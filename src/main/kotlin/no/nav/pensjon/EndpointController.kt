package no.nav.pensjon

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.ObjectWriter
import com.nimbusds.jwt.SignedJWT
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam


@Controller
class EndpointController(
    @Value("#{'\${MASKINPORTEN_SCOPES}'.split(' ')}")
    private val maskinportenScopes: List<String>,
    private val maskinportenTokenService: MaskinportenTokenService,
) {
    private val mapper = ObjectMapper()
    private val prettyPrinter: ObjectWriter = mapper.writerWithDefaultPrettyPrinter()

    val scopeTree: Map<String, Map<String, List<String>>> = maskinportenScopes
        .sorted()
        .groupBy { it.split(":")[0] }
        .map {
            it.key to it.value.groupBy { scope ->
                val split = scope.split(":", "/")
                if (split.size > 1) split[1] else split[0]
            }
        }.toMap()

    @GetMapping("/")
    fun index(
        model: Model,
        @RequestParam("scopes", required = false) scopes: List<String>?,
        @AuthenticationPrincipal principal: DefaultOidcUser
    ): String {
        model.addAttribute("scopeTree", scopeTree)
        try {
            scopes?.let {
                val accessToken = maskinportenTokenService.accessToken(it, emptyList(), null)
                model.addAttribute("token", accessToken)

                try {
                    val jwt = SignedJWT.parse(accessToken)
                    val payload = mapper.readTree(jwt.parsedParts[1].decodeToString())
                    model.addAttribute(
                        "payload",
                        prettyPrinter.writeValueAsString(payload)
                    )
                    model.addAttribute(
                        "expires",
                        mapper.readTree(jwt.parsedParts[1].decodeToString()).get("exp").toString()
                    )
                } catch (_: Exception) {
                }
            }
        } catch (e: MaskinportenTokenService.MaskinportenException) {
            model.addAttribute("error", e.message)
            model.addAttribute("errorResponse", e.errorResponse)
        } catch (e: Exception) {
            model.addAttribute("error", e.toString())
        }
        return "index"
    }
}
