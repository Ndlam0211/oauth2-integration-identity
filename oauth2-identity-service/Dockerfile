# stage 1: build the application
# use the official Maven image with Amazon Corretto 17 to build the application.
# This image includes both Maven and the JDK, which allows to compile and package Java application in one step.
FROM maven:3.9.14-amazoncorretto-17 AS build

# set the working directory in the container to /app
# and copy the pom.xml and source code to the container
WORKDIR /app
COPY pom.xml .
COPY src ./src

# format code using Spotless plugin before building the application
RUN mvn spotless:apply

# build application and skip tests
RUN mvn package -DskipTests

# stage 2: run the application
# This image includes only the JDK, which is sufficient to run the compiled Java application.
FROM amazoncorretto:17.0.18

# set the working directory in the container to /app
# and copy the built jar file from the build image that was created above
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
