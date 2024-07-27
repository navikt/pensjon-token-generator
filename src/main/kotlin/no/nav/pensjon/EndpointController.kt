package no.nav.pensjon

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
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
    ): String {
        val scopeTree: Map<String, Map<String, List<String>>> = maskinportenScopes
            .sorted()
            .groupBy { it.split(":")[0] }
            .map {
                it.key to it.value.groupBy { scope ->
                    val split = scope.split(":", "/")
                    if (split.size > 1) split[1] else split[0]
                }
            }.toMap()

        model.addAttribute("scopeTree", scopeTree)
        return "index"
    }

    @GetMapping("/token")
    fun getToken(@RequestParam("scopes") scopes: List<String>): ResponseEntity<String> =
        ok(maskinportenTokenService.accessToken(scopes, emptyList(), null))
}
