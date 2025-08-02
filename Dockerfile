FROM eclipse-temurin:17.0.10-jdk AS builder
WORKDIR /app

COPY gradlew ./
COPY gradle ./gradle
COPY build.gradle ./
COPY settings.gradle ./
COPY src ./src

RUN chmod +x ./gradlew
RUN ./gradlew clean bootJar --no-daemon -DskipTests

FROM eclipse-temurin:17.0.10-jre
WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar
COPY src/main/resources/application-prod.yml /app/config/application-prod.yml

ENTRYPOINT ["java", "-XX:+HeapDumpOnOutOfMemoryError", "-XX:HeapDumpPath=/tmp/heapdump.hprof", "-XX:NativeMemoryTracking=summary", "-Xmx512m", "-Xms128m", "-jar", "app.jar", "--spring.config.additional-location=classpath:/,file:/app/config/"]
