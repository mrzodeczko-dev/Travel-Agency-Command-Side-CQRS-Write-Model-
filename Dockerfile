FROM jelastic/maven:3.9.9-openjdk-21.0.2-almalinux-9 AS build
WORKDIR /home/app
COPY . .
RUN mvn clean package -DskipTests

FROM amazoncorretto:21
WORKDIR /home/app
COPY --from=build /home/app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/home/app/app.jar"]