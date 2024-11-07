pipeline {
    agent any

    environment {
        SONAR_HOST_URL = 'http://10.0.2.15:9000/'
        SONAR_TOKEN = credentials('sonar-token')
        NEXUS_URL = 'http://localhost:8081'
        NEXUS_REPOSITORY = 'maven-releases'
        NEXUS_GROUP = 'tn.esprit.spring'
        NEXUS_ARTIFACT = 'kaddem'
        NEXUS_VERSION = '0.0.1'
        NEXUS_CREDENTIALS = credentials('nexus-credentials')
        DOCKERHUB_CREDENTIALS = credentials('docker-hub')
        DOCKER_IMAGE = 'manar044/kaddem'  // Default image name
        IMAGE_TAG = "${env.BUILD_NUMBER ?: 'latest'}"  // Default to 'latest' if BUILD_NUMBER is not set
    }

    stages {
        stage('Git') {
            steps {
                echo 'Pulling from Git'
                git branch: 'Manar', url: 'https://github.com/ismailgharnougui/Devops'
            }
        }

        stage('Maven') {
            steps {
                echo 'Running Maven Clean Install'
                sh 'mvn clean install'
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

        stage('Docker Image Registries') {
            parallel {
                stage('Push Docker Image to DockerHub') {
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

                stage('Docker Image Push To Nexus') {
                    steps {
                        echo 'Pushing Docker Image to Nexus'
                        withCredentials([usernamePassword(credentialsId: 'nexus-credentials', usernameVariable: 'NEXUS_USER', passwordVariable: 'NEXUS_PASS')]) {
                            script {
                                sh '''
                                    docker login -u $NEXUS_USER -p $NEXUS_PASS $NEXUS_URL
                                    docker tag $DOCKER_IMAGE:$IMAGE_TAG $NEXUS_URL/repository/docker-hosted/$DOCKER_IMAGE:$IMAGE_TAG
                                    docker push $NEXUS_URL/repository/docker-hosted/$DOCKER_IMAGE:$IMAGE_TAG
                                '''
                            }
                        }
                    }
                }
            }
        }

        stage('Clean Up Docker Images') {
            steps {
                echo 'Cleaning up Docker images'
                sh '''
                    docker rmi $DOCKER_IMAGE:$IMAGE_TAG
                    docker system prune -f
                '''
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
