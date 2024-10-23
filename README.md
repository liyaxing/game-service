# Game Service Spring Boot Application

This project is a Spring Boot application that demonstrates various functionalities and configurations. It includes multithreading, batch processing, and integration with a MySQL database.

## Table of Contents

- [Build the Application](#build-the-application)
- [Run the Application](#run-the-application)
    - [Running the JAR Directly](#running-the-jar-directly)
    - [Building and Running with Docker](#building-and-running-with-docker)
    - [Using Docker Compose](#using-docker-compose)

## Build the Application

To build the application, you'll need Maven installed on your machine. You can skip the tests to speed up the process.

```sh
mvn clean package -DskipTests
```

This command will create a JAR file in the target/ directory.

## Run the Application
### Running the JAR Directly
You can run the JAR file directly by specifying the necessary environment variables for the datasource configuration.

```sh
java -jar target/game-service-0.0.1-SNAPSHOT.jar --spring.datasource.url=jdbc:mysql://localhost:13306/mydatabase --spring.datasource.username=myuser --spring.datasource.password=secret
```

Alternatively, set the environment variables and then run the JAR.

```sh
export SPRING_DATASOURCE_URL=jdbc:mysql://localhost:13306/mydatabase
export SPRING_DATASOURCE_USERNAME=myuser
export SPRING_DATASOURCE_PASSWORD=secret
java -jar target/game-service-0.0.1-SNAPSHOT.jar
```

### Building and Running with Docker
To build and run the Docker container for the application, you'll need Docker installed on your machine.

1. Build the Docker Image

```sh
docker build -t game-service .
```

2. Run the Docker Container
```sh
docker run -e SPRING_DATASOURCE_URL=jdbc:mysql://localhost:13306/mydatabase -e SPRING_DATASOURCE_USERNAME=myuser -e SPRING_DATASOURCE_PASSWORD=secret -p 8080:8080 game-service
```

### Using Docker Compose
You can use Docker Compose to start the application and its dependencies (e.g., MySQL database).

The docker-compose.yml file is included in the project.

1. Build and Run the Containers
```sh
docker compose -f docker-compose.yml up --build
```

This command will build the images and start the containers as defined in the docker-compose.yml file.