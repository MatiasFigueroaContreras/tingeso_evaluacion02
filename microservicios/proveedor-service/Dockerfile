FROM openjdk:17
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} proveedor-service-1.jar
ENTRYPOINT ["java", "-jar", "proveedor-service-1.jar"]