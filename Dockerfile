FROM gcr.io/distroless/java21-debian12:nonroot

ENV LOGGING_CONFIG=classpath:logback-nais.xml
ENV TZ="Europe/Oslo"

COPY build/libs/pensjon-maskinporten-test-1.0.jar /app/app.jar
WORKDIR /app

CMD ["app.jar"]
