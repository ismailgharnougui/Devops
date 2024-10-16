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
                def mvn = tool 'Default Maven'
                withSonarQubeEnv() {
                    sh "${mvn}/bin/mvn clean verify sonar:sonar -Dsonar.projectKey=Devops -Dsonar.projectName='Devops' -Dsonar.login=Mariem-token"
                }
            }
        }
        stage('Junit/Mockito') {
            steps {
                echo 'Running Junit Tests'
                sh 'mvn test'
            }
        }
        stage('Deploy to Nexus') {
            steps {
                echo 'Deploying to Nexus...'
                sh 'mvn deploy -DskipTests'
            }
        }
    }
}
