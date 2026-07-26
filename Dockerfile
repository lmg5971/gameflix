FROM amazoncorretto:21.0.12-al2023 AS build
WORKDIR /workspace

RUN dnf install --assumeyes unzip
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
COPY src/ src/
RUN chmod 0755 mvnw \
    && ./mvnw --batch-mode --no-transfer-progress clean package \
    && cp target/gameflix-*.jar application.jar

FROM amazoncorretto:21.0.12-al2023-headless
WORKDIR /app

COPY --from=build /workspace/application.jar application.jar

USER 10001:10001
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "application.jar"]