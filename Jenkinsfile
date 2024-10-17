

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

        
    }
}
}