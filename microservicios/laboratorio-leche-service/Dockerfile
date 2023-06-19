FROM openjdk:17
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} laboratorio-leche-service-1.jar
ENTRYPOINT ["java", "-jar", "laboratorio-leche-service-1.jar"]