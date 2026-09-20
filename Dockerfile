FROM gradle:8.14-jdk21 AS build

WORKDIR /app

COPY . .

RUN gradle clean build -x test

FROM openjdk:21-jdk-slim

WORKDIR /app

COPY --from=build /app/build/libs/discografia-1.war app.war

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.war"]