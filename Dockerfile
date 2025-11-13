# ---- ESTÁGIO DE BUILD (CORRIGIDO) ----
# Substituído maven:3.8.4-openjdk-17 por uma tag moderna baseada em temurin
FROM maven:3-eclipse-temurin-17 AS build
WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

# ---- ESTÁGIO DE RUNTIME (CORRIGIDO E OTIMIZADO) ----
# Substituído openjdk:17-jdk-slim pela imagem JRE (Java Runtime Environment)
# que é menor e mais segura para produção.
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Copia o artefato do estágio de build
COPY --from=build /app/target/MediasAPI-0.0.1-SNAPSHOT.jar app.jar

# Adiciona um usuário não-root para segurança
RUN groupadd --system appuser && useradd --system -g appuser appuser
USER appuser

EXPOSE 8080

# Define o entrypoint
CMD ["java", "-jar", "app.jar"]
