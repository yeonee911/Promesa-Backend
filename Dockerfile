# ────────────────────────────────────────────────────────────────
# 1) 빌드 스테이지: OpenJDK 17 + Gradle wrapper
# ────────────────────────────────────────────────────────────────
FROM openjdk:17-jdk-alpine AS builder
WORKDIR /app

# Gradle wrapper 스크립트와 프로젝트 소스 복사
COPY gradlew ./
COPY gradle ./gradle
COPY build.gradle ./
COPY settings.gradle ./
COPY src ./src

# Gradle 실행 권한 부여 및 빌드
RUN chmod +x ./gradlew
RUN ./gradlew clean bootJar --no-daemon -DskipTests

# ────────────────────────────────────────────────────────────────
# 2) 런타임 스테이지: OpenJDK 17 JRE(경량) 기반
# ────────────────────────────────────────────────────────────────
FROM openjdk:17-jdk-alpine
WORKDIR /app

# 빌드 스테이지에서 생성된 JAR만 복사
COPY --from=builder /app/build/libs/*.jar app.jar

# 컨테이너 기동 시 Spring Boot JAR 실행
ENTRYPOINT ["java", "-jar", "app.jar"]