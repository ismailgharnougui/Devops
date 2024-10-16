pipeline {
    agent any
    
    environment {
        MAVEN_HOME = 'opt/apache-maven-3.6.3' // Remplacez par le chemin d'installation de Maven si nécessaire
        NEXUS_URL = 'http://172.17.0.2:8081/repository/maven-releases/' // Remplacez par l'URL de votre dépôt Nexus
        NEXUS_USERNAME = 'admin' // Nom d'utilisateur Nexus
        NEXUS_PASSWORD = 'mustapha' // Mot de passe Nexus
    }

    stages {
        stage('Checkout from Git') {
            steps {
                echo 'Pulling from Git'
                git branch: 'mustaphaa', url: 'https://github.com/ismailgharnougui/Devops'
            }
        }
        
        stage('Testing Maven') {
            steps {
                sh "${MAVEN_HOME}/bin/mvn -version"
            }
        }

        stage('Build with Maven') {
            steps {
                echo 'Building the project with Maven'
                sh "${MAVEN_HOME}/bin/mvn clean install -DskipTests"
            }
        }

        stage('Deploy to Nexus') {
            steps {
                echo 'Deploying to Nexus...'
                sh """
                    ${MAVEN_HOME}/bin/mvn deploy \
                    -DskipTests \
                    -DaltDeploymentRepository=nexus::default::${NEXUS_URL} \
                    -Dnexus.username=${NEXUS_USERNAME} \
                    -Dnexus.password=${NEXUS_PASSWORD}
                """
            }
        }
    }
}
