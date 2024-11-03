pipeline {
    agent any
    environment {
        SONAR_HOST_URL = 'http://172.17.0.1:9000/'
        SONAR_LOGIN = credentials('Sonarqube')
        NEXUS_URL = "http://localhost:8081"
        NEXUS_REPOSITORY = "maven-releases"
        NEXUS_GROUP = "tn.esprit.spring"
        NEXUS_ARTIFACT = "kaddem"
        NEXUS_VERSION = "0.0.1"
        NEXUS_CREDENTIALS = "admin:nexus"
        DOCKER_HUB_CREDENTIALS = credentials('dockerhub-credentials')
        DOCKER_IMAGE_NAME = "aziz2205/kaddem" // Remplacez par votre nom d'image Docker Hub
    }
    stages {
        stage('Checkout from Git') {
            steps {
                echo 'Pulling from Git'
                git branch: 'azizz-hannachi', url: 'https://github.com/ismailgharnougui/Devops'
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

        stage('Coverage Report') {
            steps {
                sh '/usr/share/maven/bin/mvn verify'
            }
        }
        
        stage('SonarQube Analysis') {
            steps {
                echo 'Running SonarQube Analysis'
                withSonarQubeEnv('SonarQube') { 
                    sh 'mvn sonar:sonar -Dsonar.projectKey=Devops -Dsonar.host.url=$SONAR_HOST_URL -Dsonar.login=$SONAR_LOGIN -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml'
                }
            }
        }

        stage('JUnit/Mockito Tests') {
            steps {
                echo 'Running JUnit/Mockito Tests'
                sh 'mvn test jacoco:report'
            }
        }
        
        stage('Nexus Deployment') {
            steps {
                script {
                    def component_id = sh(
                        script: "curl -u '${NEXUS_CREDENTIALS}' '${NEXUS_URL}/service/rest/v1/components?repository=${NEXUS_REPOSITORY}&group=${NEXUS_GROUP}&name=${NEXUS_ARTIFACT}&version=${NEXUS_VERSION}' | jq -r .items[].id",
                        returnStdout: true
                    ).trim()

                    if (component_id) {
                        echo "Deleting component with ID: ${component_id}"
                        sh "curl -X DELETE -u '${NEXUS_CREDENTIALS}' '${NEXUS_URL}/service/rest/v1/components/${component_id}'"
                    } else {
                        echo "No component found with version ${NEXUS_VERSION} to delete."
                    }

                    echo 'Deploying to Nexus'
                    sh "mvn deploy -Dnexus.username=admin -Dnexus.password=nexus"
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    dockerImage = docker.build("${DOCKER_IMAGE_NAME}:latest")
                }
            }
        }

        stage('Push Docker Image to Docker Hub') {
            steps {
                script {
                    docker.withRegistry('https://registry.hub.docker.com', 'dockerhub-credentials') {
                        dockerImage.push()
                    }
                }
            }
        }

        stage('Deploy with Docker Compose') {
            steps {
                sh 'docker-compose down' // Arrête les conteneurs existants (si nécessaires)
                sh 'docker-compose up -d' // Démarre les services définis dans docker-compose.yml
            }
        }
    }
}
