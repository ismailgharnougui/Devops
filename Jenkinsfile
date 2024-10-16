pipeline {
    agent any
    
    stages {
        stage('Checkout from Git') {
            steps {
                echo 'Pulling from Git'
                git branch: 'Mariem', url: 'https://github.com/ismailgharnougui/Devops'
            }
        }
        
        stage('Maven Clean') {
            steps {
                echo 'Running Maven Clean'
                sh 'mvn clean'
            }
        }

        stage('Maven Compile') {
            steps {
                echo 'Running Maven Compile'
                sh 'mvn compile'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                echo 'Running SonarQube Analysis'
                // Assuming SonarQube is configured
                withSonarQubeEnv('SonarQube') {
                    sh 'mvn sonar:sonar'
                }
            }
        }

        stage('JUnit/Mockito Tests') {
            steps {
                echo 'Running JUnit/Mockito Tests'
                sh 'mvn test'
            }
        }

        stage('Deploy to Nexus') {
            steps {
                echo 'Deploying to Nexus...'
                sh 'mvn deploy -DskipTests -X'
            }
        }
    }
}
