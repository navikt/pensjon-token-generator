FROM ghcr.io/navikt/baseimages/temurin:21
ENV LOGGING_CONFIG=classpath:logback-nais.xml
COPY build/libs/pensjon-maskinporten-test-1.0.jar /app/app.jar
