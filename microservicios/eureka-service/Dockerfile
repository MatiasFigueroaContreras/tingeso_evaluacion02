FROM openjdk:17
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} eureka-service-1.jar
ENTRYPOINT ["java", "-jar", "/eureka-service-1.jar"]