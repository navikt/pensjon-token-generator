package no.nav.pensjon

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
class EndpointController(
    @Value("#{'\${MASKINPORTEN_SCOPES}'.split(' ')}")
    val maskinportenScopes: List<String>,
    val maskinportenTokenService: MaskinportenTokenService,
) {
    @GetMapping("/")
    fun index(
        model: Model,
        @AuthenticationPrincipal principal: DefaultOidcUser
    ): String {
        model.addAttribute("ascopes", maskinportenScopes)
        return "index"
    }

    @GetMapping("/token")
    fun getToken(@RequestParam("scopes") scopes: List<String>): ResponseEntity<String> =
        ok(maskinportenTokenService.accessToken(scopes, emptyList(), null))
}
