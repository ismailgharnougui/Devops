# Use the openjdk17 image from Docker Hub
FROM openjdk:17
# Expose the port of your Spring Boot application
EXPOSE 8080
# Copy the jar of the application into the container
ADD target/kaddem-0.0.1-20241102.174448-13.jar /app/application.jar
  # Commande pour exécuter l'application
ENTRYPOINT ["java", "-jar", "/app/application.jar"]
