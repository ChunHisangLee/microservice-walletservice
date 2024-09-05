# Use a smaller OpenJDK image for better performance and smaller size
FROM openjdk:21-jdk-slim

# Maintainer information
LABEL maintainer="jack"

# Set the working directory inside the container
WORKDIR /app

# Copy the Maven build artifact from the target directory to the container
COPY target/wallet-service-0.0.1-SNAPSHOT.jar /app/wallet-service.jar

# Expose the port the application will run on
EXPOSE 8082

# Set environment variables (can be overridden in docker-compose.yml)
ENV SPRING_DATASOURCE_URL="jdbc:postgresql://db:5432/walletdb" \
    SPRING_DATASOURCE_USERNAME="postgres" \
    SPRING_DATASOURCE_PASSWORD="Ab123456" \
    APP_JWT_SECRET="Xb34fJd9kPbvmJc84mDkV9b3Xb34fJd9kPbvmJc84mDkV9b3Xb34fJd9kPbvmJc84" \
    APP_JWT_EXPIRATION_MS="3600000" \
    SECURITY_AUTHENTICATION_ENABLED="false" \
    SPRING_RABBITMQ_HOST="rabbitmq" \
    SPRING_RABBITMQ_PORT="5672" \
    SPRING_RABBITMQ_USERNAME="guest" \
    SPRING_RABBITMQ_PASSWORD="guest" \
    SPRING_PROFILES_ACTIVE="docker"

# Ensure proper timezone is set if necessary
ENV TZ=UTC

# Add a health check to verify if the wallet service is running correctly
HEALTHCHECK --interval=30s --timeout=5s --start-period=30s --retries=3 CMD curl -f http://localhost:8082/actuator/health || exit 1

# Use exec form of CMD to ensure signals are received by the JVM process
CMD ["java", "-jar", "/app/wallet-service.jar"]
