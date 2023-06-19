FROM openjdk:17
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} pago-service-1.jar
ENTRYPOINT ["java", "-jar", "pago-service-1.jar"]