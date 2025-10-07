FROM maven:amazoncorretto AS build
WORKDIR /app
COPY pom.xml ./
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests

FROM openjdk:26-jdk-slim-bookworm AS runtime
RUN apt-get -y update; apt-get -y install curl
WORKDIR /app
COPY --from=build /app/target/server-0.0.1-SNAPSHOT.jar server-0.0.1-SNAPSHOT.jar
EXPOSE 7700
CMD ["java", "-jar", "server-0.0.1-SNAPSHOT.jar"]