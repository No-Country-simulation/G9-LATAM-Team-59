# Stage 1: Build
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app

# Copiar wrapper y archivos de definicion de gradle para cachear dependencias
COPY gradlew .
COPY gradle gradle
COPY build.gradle.kts settings.gradle.kts ./

# Dar permisos de ejecucion al Gradle Wrapper
RUN chmod +x gradlew

# Copiar el codigo fuente del backend
COPY src src

# Compilar la aplicacion ignorando los tests de construccion
RUN ./gradlew bootJar -x test

# Stage 2: Runtime
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copiar el ejecutable .jar generado en la etapa de build
COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
