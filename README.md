# Pensjon-token-generator
Applikasjonen gir team pensjon-saksbehandling muligheten til å generere opp token for test av endepunkter som krever Maskinporten-token.

Tjenesten som krever Maskinporten-token med bestemt scope må deployes før klienten som ønsker å utstede token med samme scope.

For å legge til flere scopes må `nais.yaml` filen under `maskinporten.scope.consumes` utvides: 
```
  maskinporten:
    enabled: true
    scopes:
      consumes:
        - name: nav:pensjon/v1/vedtaksinformasjon
        - name: nav:pensjon/v3/alderspensjon
        - name: nav:pensjon/afpprivat
        - name: ... scope her
```
