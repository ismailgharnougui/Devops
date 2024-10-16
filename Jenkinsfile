pipeline {
    agent any

    stages {
        stage('Checkout SCM') {
            steps {
                checkout scm
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
                // Define Maven tool
                def mvn = tool 'Maven 3.6.3'
                // Perform SonarQube analysis with the token
                withSonarQubeEnv('SonarQube') {
                    sh "${mvn}/bin/mvn clean verify sonar:sonar \
                        -Dsonar.projectKey=Devops \
                        -Dsonar.projectName='Devops' \
                        -Dsonar.branch.name=Mariem \
                        -Dsonar.host.url=http://192.168.230.140:9000 \
                        -Dsonar.login=Mariem-token"
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
