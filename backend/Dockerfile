# Etapa de compilacao: usa Maven e JDK para gerar o arquivo executavel.
FROM maven:3.9.9-eclipse-temurin-24 AS build
WORKDIR /app
COPY pom.xml ./
COPY src ./src
# Os testes de integracao rodam separadamente com um MySQL temporario.
RUN mvn -B -ntp -DskipTests package

# Etapa usada pelo compose.test.yaml, com o banco temporario disponivel.
FROM build AS test
CMD ["mvn", "-B", "-ntp", "test"]

# Imagem final: precisa apenas do Java para executar o JAR compilado.
FROM eclipse-temurin:24-jre AS runtime
WORKDIR /app
COPY --from=build /app/target/conale-backend.jar app.jar
# Executa sem privilegios de administrador dentro do container.
USER 10001:10001
# Informa a porta da aplicacao; o Compose define o acesso pela maquina.
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
