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

        stage('Maven Clean Install') {
            steps {
                echo 'Running Maven Clean Install'
                // This stage runs the Maven Clean Install command to compile the project and generate the artifacts (JAR, WAR, etc.)
                sh 'mvn clean install'
            }
        }

        stage('JUnit/Mockito Tests') {
            steps {
                echo 'Running JUnit/Mockito Tests'
                // Executes unit tests using JUnit and Mockito to ensure code quality and correctness
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
                        // Uploading the generated artifact (JAR file) to Nexus repository
                        // Configures the Nexus URL, repository, artifact information, and credentials for deployment
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
                        // Builds a Docker image using the Dockerfile in the workspace
                        // Logs into Docker Hub with credentials for subsequent image push
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
                                // Pushes the built Docker image to Docker Hub
                                // Ensures Docker Hub login for secure upload of the image
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
                                // Logs into Nexus Docker registry and tags the Docker image for Nexus repository
                                // Pushes the Docker image to Nexus for hosting in a private repository
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
                // Removes the locally built Docker image and prunes unused Docker resources
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
