pipeline {
    agent any
    environment {
        SONAR_HOST_URL = 'http://10.0.2.15:9000/'
        SONAR_TOKEN = credentials('sonar-token')  // Reference to the SonarQube token credential
    }

    stages {
        stage('Checkout from Git') {
            steps {
                echo 'Pulling from Git'
                git branch: 'Manar', url: 'https://github.com/ismailgharnougui/Devops'
            }
        }

        stage('Maven Clean Install') {
            steps {
                echo 'Running Maven Clean Install'
                sh 'mvn clean install'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                echo 'Running SonarQube Analysis'
                withSonarQubeEnv('SonarQube') {
                    sh '''
                        mvn sonar:sonar \
                            -Dsonar.projectKey=Devops \
                            -Dsonar.host.url=$SONAR_HOST_URL \
                            -Dsonar.token=$SONAR_TOKEN
                    '''
                }
            }
        }

        stage('JUnit/Mockito Tests') {
            steps {
                echo 'Running JUnit/Mockito Tests'
                sh 'mvn test'
            }
        }
    }

    post {
        always {
            echo 'Pipeline completed. Cleaning up...'
        }
        success {
            echo 'Pipeline finished successfully!'
        }
        failure {
            echo 'Pipeline failed. Checking logs for errors.'
        }
        unstable {
            echo 'Pipeline was unstable.'
        }
    }
}
