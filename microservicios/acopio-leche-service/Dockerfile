FROM openjdk:17
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} acopio-leche-service-1.jar
ENTRYPOINT ["java", "-jar", "/acopio-leche-service-1.jar"]