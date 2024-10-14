pipeline {
    agent any
    
    stages {
        stage('Checkout from Git') {
            steps {
                echo 'Pulling from Git'
                git branch: 'Eya', url: 'https://github.com/ismailgharnougui/Devops'
            }
        }
        
        stage('Testing Maven') {
            steps {
                sh 'mvn -version'
            }
        }
