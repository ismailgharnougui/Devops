# Étape 1 : Construire l'application
FROM maven:3.8.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copier le fichier pom.xml et télécharger les dépendances Maven
COPY pom.xml ./
RUN mvn dependency:go-offline -B

# Copier le code source et compiler le projet
COPY src ./src
RUN mvn clean package -DskipTests

# Étape 2 : Créer l'image exécutable pour l'application
FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp

# Copier le fichier JAR généré depuis l'étape de construction
COPY --from=build /app/target/*.jar app.jar

# Exposer le port 8080 pour l'application Spring Boot
EXPOSE 8080

# Commande pour lancer l'application
ENTRYPOINT ["java", "-jar", "/app.jar"]
