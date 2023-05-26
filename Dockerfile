FROM ghcr.io/navikt/baseimages/temurin:17
COPY build/libs/pensjon-maskinporten-test-1.0-plain.jar /app/app.jar
