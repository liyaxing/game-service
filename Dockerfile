#################################
# Dockerfile for game-service #
#################################

FROM    eclipse-temurin:21-jdk-alpine
LABEL   app.name="game-service"

ENV     APP_BASE_DIR    "/opt/vanguard"
ENV     WORKDIR         "${APP_BASE_DIR}/game-service"

WORKDIR ${WORKDIR}

COPY target/*.jar app.jar

ENTRYPOINT ["java","-jar","app.jar"]