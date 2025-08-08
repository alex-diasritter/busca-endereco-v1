# syntax=docker/dockerfile:1

# 1) Stage de build: pega Maven + JDK
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# 1.1) Cacheia as dependências do Maven
COPY pom.xml .
RUN mvn dependency:go-offline -B

# 1.2) Copia o código-fonte e gera o jar (sem testes)
COPY src ./src
RUN mvn clean package -B -DskipTests

# 2) Stage de runtime: JRE puro, menor
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# 2.1) Copia o jar gerado no stage de build
COPY --from=build /app/target/buscacep-*.jar app.jar

# 2.2) Porta exposta pela aplicação
EXPOSE 8080

# 2.3) Comando de inicialização com perfil ativo "prod"
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=prod"]
