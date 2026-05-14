FROM eclipse-temurin:23-jdk-alpine AS build
WORKDIR /app
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN chmod +x mvnw && ./mvnw dependency:resolve -q
COPY src/ src/
RUN ./mvnw package -DskipTests -q

FROM eclipse-temurin:23-jdk-alpine
COPY --from=build /app/target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]