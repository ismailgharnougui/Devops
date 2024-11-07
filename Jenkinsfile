pipeline {
    agent any

    environment {
        SONAR_HOST_URL = 'http://10.0.2.15:9000/'
        SONAR_TOKEN = credentials('sonar-token')
        NEXUS_URL = 'http://localhost:8081'
        NEXUS_REPOSITORY = 'maven-releases'
        NEXUS_GROUP = 'tn.esprit.spring'
        NEXUS_ARTIFACT = 'kaddem'
<<<<<<< HEAD
        NEXUS_VERSION = '0.0.1'
        NEXUS_CREDENTIALS = credentials('nexus-credentials')
        DOCKERHUB_CREDENTIALS = credentials('docker-hub')
        DOCKER_IMAGE = 'manar044/kaddem'
        IMAGE_TAG = "${env.BUILD_NUMBER}"  // Dynamic image tag based on build number
=======
        NEXUS_VERSION = '0.0.1'  // Replace with your artifact version
        NEXUS_CREDENTIALS = credentials('nexus-credentials')  // Reference to the Nexus credentials
        DOCKERHUB_CREDENTIALS = credentials('docker-hub')  // DockerHub credentials reference
        DOCKER_IMAGE = 'manar044/kaddem:latest'  // Your Docker image name
>>>>>>> 86766703c096786b83a12d75c61f3e22e59f4432
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

        // Multi-Stage Docker Build: Optimized Docker Build
        stage('Build Docker Image') {
            steps {
                echo 'Building Docker Image'
<<<<<<< HEAD
                withCredentials([usernamePassword(credentialsId: 'docker-hub', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    script {
                        // Corrected Docker build command with updated syntax
                        sh '''
                            docker login -u $DOCKER_USER -p $DOCKER_PASS
                            sudo docker build --tag $DOCKER_IMAGE:$IMAGE_TAG -f dockerfile .
                        '''
                    }
=======
                withCredentials([usernamePassword(credentialsId: 'docker hub', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    sh '''
                        echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin
                        sudo docker build -t $DOCKER_IMAGE .
                    '''
>>>>>>> 86766703c096786b83a12d75c61f3e22e59f4432
                }
            }
        }

<<<<<<< HEAD
        // Push Docker Image to DockerHub and Nexus in Parallel
        stage('Push Docker Image to Registries') {
            parallel {
                stage('Push Docker Image to DockerHub') {
                    steps {
                        echo 'Pushing Docker Image to DockerHub'
                        withCredentials([usernamePassword(credentialsId: 'docker-hub', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                            script {
                                sh '''
                                    echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin
                                    sudo docker push $DOCKER_IMAGE:$IMAGE_TAG
                                '''
                            }
                        }
                    }
=======
        stage('Push Docker Image to DockerHub') {
            steps {
                echo 'Pushing Docker Image to DockerHub'
                withCredentials([usernamePassword(credentialsId: 'docker hub', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    sh '''
                        echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin
                        sudo docker push $DOCKER_IMAGE
                    '''
>>>>>>> 86766703c096786b83a12d75c61f3e22e59f4432
                }

                stage('Push Docker Image to Nexus') {
                    steps {
                        echo 'Pushing Docker Image to Nexus'
                        withCredentials([usernamePassword(credentialsId: 'nexus-credentials', usernameVariable: 'NEXUS_USER', passwordVariable: 'NEXUS_PASS')]) {
                            script {
                                sh '''
                                    docker login -u $NEXUS_USER -p $NEXUS_PASS $NEXUS_URL
                                    sudo docker tag $DOCKER_IMAGE:$IMAGE_TAG $NEXUS_URL/repository/docker-hosted/$DOCKER_IMAGE:$IMAGE_TAG
                                    sudo docker push $NEXUS_URL/repository/docker-hosted/$DOCKER_IMAGE:$IMAGE_TAG
                                '''
                            }
                        }
                    }
                }
            }
        }

        // Clean Up Docker Images to Save Space
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
            echo 'Pipeline was unstable..'
        }
    }
}
