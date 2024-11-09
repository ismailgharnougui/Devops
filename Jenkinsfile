pipeline {
    agent any

    environment {
        SONAR_HOST_URL = 'http://10.0.2.15:9000/'
        SONAR_TOKEN = credentials('sonar-token')
        NEXUS_URL = 'http://127.0.0.1:8081'
        NEXUS_REPOSITORY = 'maven-releases'
        NEXUS_GROUP = 'tn.esprit.spring'
        NEXUS_ARTIFACT = 'kaddem'
        NEXUS_VERSION = '0.0.1'
        NEXUS_CREDENTIALS = credentials('nexus-credentials')
        DOCKERHUB_CREDENTIALS = credentials('docker-hub')
        DOCKER_IMAGE = 'manar044/kaddem-manar-app'
        IMAGE_TAG = "${env.BUILD_NUMBER ?: 'latest'}"
        SPRING_APP_URL = 'http://localhost:8089/kaddem/actuator/prometheus'
    }

    stages {
        stage('Git') {
            steps {
                echo 'Pulling from Git'
                git branch: 'Manar', url: 'https://github.com/ismailgharnougui/Devops'
            }
        }

        stage('Maven Clean') {
            steps {
                echo 'Running Maven Clean Install'
                sh 'mvn clean install'
            }
        }

        stage('JUnit/Mockito') {
            steps {
                echo 'Running JUnit/Mockito Tests'
                sh 'mvn test'
            }
        }

        stage('SonarQube') {
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

        stage('Nexus') {
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

        stage('Docker Build') {
            steps {
                echo 'Building Docker Image'
                withCredentials([usernamePassword(credentialsId: 'docker-hub', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    script {
                        sh '''
                            echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin
                            docker build --tag $DOCKER_IMAGE:$IMAGE_TAG -f dockerfile .
                        '''
                    }
                }
            }
        }

        stage('Docker Image into DockerHub') {
            steps {
                echo 'Pushing Docker Image to DockerHub'
                withCredentials([usernamePassword(credentialsId: 'docker-hub', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    script {
                        sh '''
                            echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin
                            docker push $DOCKER_IMAGE:$IMAGE_TAG
                        '''
                    }
                }
            }
        }

        stage('Docker Compose') {
            steps {
                echo 'Starting Docker containers'
                sh 'docker compose up -d'
            }
        }
    }

    post {
        success {
            echo 'Pipeline finished successfully!'
            emailext(
                subject: "Pipeline Notification: ${env.JOB_NAME} Build #${env.BUILD_NUMBER} - SUCCESS",
                body: """
                    <h3>Pipeline Notification</h3>
                    <p><b>Project:</b> ${env.JOB_NAME}</p>
                    <p><b>Build Number:</b> ${env.BUILD_NUMBER}</p>
                    <p><b>Status:</b> SUCCESS</p>
                    <p><a href="${env.BUILD_URL}">View Build Details</a></p>
                """,
                to: 'manarwahada177@gmail.com',
                replyTo: 'manarwahada177@gmail.com',
                mimeType: 'text/html'
            )
        }
        failure {
            echo 'Pipeline failed. Checking logs for errors.'
            emailext(
                subject: "Pipeline Notification: ${env.JOB_NAME} Build #${env.BUILD_NUMBER} - FAILURE",
                body: """
                    <h3>Pipeline Notification</h3>
                    <p><b>Project:</b> ${env.JOB_NAME}</p>
                    <p><b>Build Number:</b> ${env.BUILD_NUMBER}</p>
                    <p><b>Status:</b> FAILURE</p>
                    <p><a href="${env.BUILD_URL}">View Build Details</a></p>
                """,
                to: 'manarwahada177@gmail.com',
                replyTo: 'manarwahada177@gmail.com',
                mimeType: 'text/html'
            )
        }
        unstable {
            echo 'Pipeline was unstable.'
            emailext(
                subject: "Pipeline Notification: ${env.JOB_NAME} Build #${env.BUILD_NUMBER} - UNSTABLE",
                body: """
                    <h3>Pipeline Notification</h3>
                    <p><b>Project:</b> ${env.JOB_NAME}</p>
                    <p><b>Build Number:</b> ${env.BUILD_NUMBER}</p>
                    <p><b>Status:</b> UNSTABLE</p>
                    <p><a href="${env.BUILD_URL}">View Build Details</a></p>
                """,
                to: 'manarwahada177@gmail.com',
                replyTo: 'manarwahada177@gmail.com',
                mimeType: 'text/html'
            )
        }
        always {
            echo 'Pipeline completed. Cleaning up...'
        }
    }
}
