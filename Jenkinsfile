pipeline {
agent any
environment {
SONAR_HOST_URL = 'http://172.17.0.3:9000/'
SONAR_LOGIN = credentials('sonar1')
NEXUS_URL = "http://172.17.0.2:8081"
NEXUS_REPOSITORY = "maven-releases"
NEXUS_GROUP = "tn.esprit.spring"
NEXUS_ARTIFACT = "kaddem"
NEXUS_VERSION = "0.0.1"
NEXUS_LOGIN  = credentials('nexus')
DOCKER_CREDENTIALS = credentials('docker1') // Your Docker registry credentials
NEXUS_CREDENTIALS  = credentials('nexus')
   
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
    stage('Coverage Report') {
        steps {
            // Generate the Jacoco coverage report
            sh '/usr/share/maven/bin/mvn verify'
        }
    }

    stage('SonarQube Analysis') {
        steps {
            echo 'Running SonarQube Analysis'
            withSonarQubeEnv('SonarQube-Server') { 
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
                        sh "curl -X DELETE -u '${NEXUS_CREDENTIALSS}' '${NEXUS_URL}/service/rest/v1/components/${component_id}'"
                    } else {
                        echo "No component found with version ${NEXUS_VERSION} to delete."
                    }
                    echo 'Deploying to Nexus'
                    sh "mvn deploy -Dnexus.username=admin -Dnexus.password=mustapha"
                }
            }
        }
  stage('Test Docker Access') {
            steps {
                echo 'Testing Docker access...'
                sh 'sudo docker images'
            }
        }
   stage('Package Application') {
            steps {
                echo 'Packaging application...'
                sh 'mvn package -DskipTests' // Cette commande génère le fichier JAR dans le dossier target
            }
        }
   
          stage('Build Docker Image') {
            steps {
                echo 'Building Docker image...'
                script {
                   
                      sh 'sudo docker build -t mustapha849/alpine:1.0.0 -f dockerfile .'
                    }
                }
            }
        

     stage('Push DockerHub') {
            steps {
                echo 'Pushing Docker image...'
                script {
                    withCredentials([usernamePassword(credentialsId: 'docker1', usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'DOCKER_PASSWORD')]) {
                    
                // Connexion à Docker
                sh 'echo $DOCKER_PASSWORD | docker login -u $DOCKER_USERNAME --password-stdin'
                
           
                sh 'sudo docker push mustapha849/alpine:1.0.0'
                    }
                }
            }
        }
         stage('Docker Compose') {
            steps {
             script {
            sh'sudo docker compose pull'
            sh 'sudo docker compose down'
            sh 'sudo docker compose up -d'
            }
         }
        }

    }
}
