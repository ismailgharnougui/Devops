# Use the openjdk17 image from Docker Hub
FROM openjdk:17
# Expose the port of your Spring Boot application
EXPOSE 8080
# Copy the jar of the application into the container
ADD target/kaddem-0.0.1.jar kaddem-0.0.1.jar
  # Commande pour exécuter l'application
ENTRYPOINT ["java", "-jar", "/kaddem-0.0.1.jar"]
