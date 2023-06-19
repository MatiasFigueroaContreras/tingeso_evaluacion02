FROM openjdk:17
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} gateway-service-1.jar
ENTRYPOINT ["java", "-jar", "/gateway-service-1.jar"]