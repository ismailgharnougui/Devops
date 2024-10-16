pipeline {
    agent any
        environment {
        SONAR_HOST_URL = 'http://192.168.230.140:9000/'
        SONAR_LOGIN = credentials('sonar')
    }
    
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
                //echo 'Running SonarQube Analysis'
                withSonarQubeEnv('SonarQube-Server') { 
                        sh 'mvn sonar:sonar -Dsonar.projectKey=Devops-Dsonar.host.url=$SONAR_HOST_URL -Dsonar.login=$SONAR_LOGIN'

               
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
