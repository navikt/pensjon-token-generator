FROM europe-north1-docker.pkg.dev/cgr-nav/pull-through/nav.no/jre:openjdk-26

ENV LOGGING_CONFIG=classpath:logback-nais.xml
ENV TZ="Europe/Oslo"

COPY build/libs/pensjon-maskinporten-test-1.0.jar /app/app.jar
WORKDIR /app

CMD ["-jar", "app.jar"]
