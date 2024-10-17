

    pipeline {
    agent any
        environment {
        SONAR_HOST_URL = 'http://172.17.0.2:9000/'
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

        stage('SonarQube Analysis') {
            steps {
                //echo 'Running SonarQube Analysis'
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
                //sh 'mvn deploy -DskipTests -X'
               
        // Use the withCredentials block to inject the Nexus credentials
         withCredentials([usernamePassword(credentialsId: 'nexus', passwordVariable: 'NEXUS_PASSWORD', usernameVariable: 'NEXUS_USERNAME')]) {
            // Execute the Maven deploy command
            sh 'mvn deploy -DskipTests -Dusername=$NEXUS_USERNAME -Dpassword=$NEXUS_PASSWORD'
            }
        }
    }
}
}