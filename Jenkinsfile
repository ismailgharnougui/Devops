pipeline {
    agent any

    stages {
        stage('Checkout from Git') {
            steps {
                echo 'Pulling from Git...'
                git branch: 'Mariem', url: 'https://github.com/ismailgharnougui/Devops'
            }
        }

        stage('Testing Maven') {
            steps {
                echo 'Testing Maven...'
                sh 'mvn -version'
            }
        }

        stage('Build Maven Project') {
            steps {
                echo 'Building Maven project...'
                sh 'mvn clean install -e -X' // Keeping your local build command
            }
        }

        stage('Deploy to Nexus') {
            steps {
                echo 'Deploying to Nexus...'
                // Run mvn deploy and skip tests
                sh 'mvn deploy -DskipTests'
            }
        }
    }
}
