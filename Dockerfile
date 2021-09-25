FROM openjdk:11-jre-slim

ENV SPRING_PROFILES_ACTIVE=prod

ARG DEPENDENCY=target/dependency
COPY ${DEPENDENCY}/BOOT-INF/classes /app
COPY ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY ${DEPENDENCY}/META-INF /app/META-INF

ENTRYPOINT ["java", "-cp", "app:app/lib/*", "com.codegrade.restapi.RestApiApplication"]

