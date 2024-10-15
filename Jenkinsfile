pipeline {
    agent any
    
    stages {
        stage('Checkout from Git') {
            steps {
                echo 'Pulling from Git'
                git branch: 'mustaphaa', url: 'https://github.com/ismailgharnougui/Devops'
            }
        }
        
        stage('Testing Maven') {
            steps {
                echo 'Checking Maven version...'
                sh 'mvn -version' // Vérifie la version de Maven
            }
        }

        stage('Build the Application') {
            steps {
                echo 'Building the application...'
                sh 'mvn clean package' // Construire l'application
            }
        }

        stage('Run the Application') {
            steps {
                echo 'Running the application...'
                sh 'java -jar target/my-spring-boot-app-0.0.1-SNAPSHOT.jar' // Exécuter le JAR
            }
        } // Close the Run the Application stage
    } // Close the stages block

    post {
        success {
            echo 'Build and run successful!'
        }
        failure {
            echo 'Build failed. Please check the logs.'
        }
    }
}
