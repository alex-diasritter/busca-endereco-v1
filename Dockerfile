# Estágio de construção (Build)
FROM maven:3.8.8-eclipse-temurin-17 AS build

WORKDIR /app
COPY pom.xml .
COPY src ./src

# Compila o projeto e gera o .jar
RUN mvn clean install -DskipTests

# ----------------------------
# Estágio de execução (Runtime)
FROM eclipse-temurin:17-jre

WORKDIR /app

# Copia o .jar do estágio "build" para o diretório atual
COPY --from=build /app/target/*.jar app.jar

EXPOSE 9090

CMD ["java", "-jar", "app.jar"]