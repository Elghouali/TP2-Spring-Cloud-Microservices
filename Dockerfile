FROM maven:3.9.9-eclipse-temurin-21 AS builder
WORKDIR /build
COPY pom.xml ./
COPY discovery-service ./discovery-service
COPY config-service ./config-service
COPY customer-service ./customer-service
COPY inventory-service ./inventory-service
COPY billing-service ./billing-service
COPY gateway-service ./gateway-service
ARG MODULE
RUN mvn -B -pl ${MODULE} -am -DskipTests package
FROM eclipse-temurin:21-jre
WORKDIR /app
ARG MODULE
COPY --from=builder /build/${MODULE}/target/${MODULE}-1.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
