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
                //echo 'Running SonarQube Analysis'
                withSonarQubeEnv('SonarQube-Server') {  // Utilisez le nom "SonarQube-Server"
                        sh 'mvn sonar:sonar -Dsonar.projectKey=Devops -Dsonar.host.url=http://192.168.230.140:9000/tutorials?id=Devops -Dsonar.login=sonar'
               
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
