package no.nav.pensjon

import no.nav.pensjonsamhandling.maskinporten.client.exceptions.MaskinportenClientException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.internalServerError
import org.springframework.http.ResponseEntity.ok
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
class EndpointController(
    val clientConfig: ClientConfig
) {
    val log: Logger = LoggerFactory.getLogger(javaClass)

    @GetMapping("/")
    fun index(
        model: Model,
        @AuthenticationPrincipal principal: DefaultOidcUser
    ): String {
        model.addAttribute("ascopes", clientConfig.scopes)
        return "index"
    }

    @GetMapping("/token")
    fun getToken(@RequestParam("scopes") scopes: Array<String>): ResponseEntity<String> = try {
        ok(clientConfig.client.getTokenString(*scopes))
    } catch(e: MaskinportenClientException) {
        log.error("Error fetching token.", e)
        internalServerError().build()
    }
}
