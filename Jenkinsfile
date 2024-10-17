
pipeline {
    agent any
    environment {
        SONAR_HOST_URL = 'http://172.17.0.3:9000/'
        SONAR_LOGIN = credentials('sonarr')
        NEXUS_HOST_URL = 'http://172.17.0.2:8081/'
        NEXUS_LOGIN = credentials('nexus')
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
                sh 'mvn -version'
            }
        }
        stage('SonarQube Analysis') {
            steps {
                echo 'Running SonarQube Analysis'
                withSonarQubeEnv('SonarQube-Server') { 
                    sh 'mvn sonar:sonar -Dsonar.projectKey=Devops -Dsonar.host.url=$SONAR_HOST_URL -Dsonar.login=$SONAR_LOGIN'
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
                // Run mvn deploy and skip tests
                sh 'mvn deploy -DskipTests'
            }
        } // Close the Deploy to Nexus stage
    } // Close the stages block
}
