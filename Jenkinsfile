pipeline {
    agent any
    environment {
        SONAR_HOST_URL = 'http://10.0.2.15:9000/'
        SONAR_TOKEN = credentials('sonar-token')  // Reference to the SonarQube token credential
        NEXUS_URL = 'http://localhost:8081'
        NEXUS_REPOSITORY = 'maven-releases'  // Replace with your Nexus repository name
        NEXUS_GROUP = 'tn.esprit.spring'
        NEXUS_ARTIFACT = 'kaddem'
        NEXUS_VERSION = '0.0.1'  // Replace with your artifact version
        NEXUS_CREDENTIALS = credentials('nexus-credentials')  // Reference to the Nexus credentials
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

        stage('Deploy to Nexus') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'nexus-credentials', usernameVariable: 'NEXUS_USER', passwordVariable: 'NEXUS_PASS')]) {
                        echo 'Deploying to Nexus'
                        sh '''
                            mvn deploy:deploy-file \
                                -Durl=${NEXUS_URL}/repository/${NEXUS_REPOSITORY} \
                                -DrepositoryId=nexus \
                                -DgroupId=${NEXUS_GROUP} \
                                -DartifactId=${NEXUS_ARTIFACT} \
                                -Dversion=${NEXUS_VERSION} \
                                -Dpackaging=jar \
                                -Dfile=target/${NEXUS_ARTIFACT}-${NEXUS_VERSION}.jar \
                                -DgeneratePom=true \
                                -Dusername=${NEXUS_USER} \
                                -Dpassword=${NEXUS_PASS}
                        '''
                    }
                }
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
