# ============================
# BUILD STAGE
# ============================
FROM eclipse-temurin:21-jdk AS builder

WORKDIR /app

COPY pom.xml mvnw ./
COPY .mvn .mvn
RUN ./mvnw dependency:go-offline

COPY src ./src

RUN ./mvnw clean package -DskipTests

# ============================
# RUNTIME STAGE
# ============================
FROM eclipse-temurin:21-jre

RUN useradd -ms /bin/bash spring

WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

USER spring

EXPOSE 8080

ENV JAVA_OPTS="-Xms256m -Xmx512m"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
