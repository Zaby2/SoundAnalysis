FROM eclipse-temurin:23-jre-alpine

RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

COPY target/*.jar app.jar

ENV JAVA_OPTS=""
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app.jar"]