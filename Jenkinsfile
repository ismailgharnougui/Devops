pipeline {
    agent any
        environment {
        SONAR_HOST_URL = 'http://192.168.230.140:9000/'
        SONAR_LOGIN = credentials('sonar')
        NEXUS_HOST_URL = 'http://192.168.230.140:8081/'
        NEXUS_LOGIN = credentials('deploymentRepoo')
       DOCKER_CREDENTIALS = credentials('docker') // Your Docker registry credentials
    }
    
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
         stage('Coverage Report') {
            steps {
                // Generate the Jacoco coverage report
                sh '/usr/share/maven/bin/mvn verify'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                //echo 'Running SonarQube Analysis'
                withSonarQubeEnv('SonarQube-Server') { 
                        sh 'mvn sonar:sonar -Dsonar.projectKey=Devops -Dsonar.host.url=$SONAR_HOST_URL -Dsonar.login=$SONAR_LOGIN'
            
                }
            }
        }

        stage('JUnit/Mockito Tests') {
            steps {
                echo 'Running JUnit/Mockito Tests'
                sh 'mvn test jacoco:report'
            }
        }

        stage('Deploy to Nexus') {
            steps {
                echo 'Deploying to Nexus...'
                //sh 'mvn deploy -DskipTests -X'
         
         withCredentials([usernamePassword(credentialsId: 'deploymentRepoo', passwordVariable: 'NEXUS_PASSWORD', usernameVariable: 'NEXUS_USERNAME')]) {
         
            sh 'mvn deploy -DskipTests -Dusername=$NEXUS_USERNAME -Dpassword=$NEXUS_PASSWORD'
             
            }
        }
    }
          stage('Test Docker Access') {
            steps {
                echo 'Testing Docker access...'
                sh 'docker images'
            }
        }
   
          stage('Build Docker Image') {
            steps {
                echo 'Building Docker image...'
                script {
                    dir('Desktop/docker') {
                             //sh 'ls -l'
                      sh 'docker build -t mariemkhamassi/alpine:1.0.0 -f Dockerfile .'
                    }
                }
            }
        }

     stage('Push Docker Image') {
            steps {
                echo 'Pushing Docker image...'
                script {
                    withCredentials([usernamePassword(credentialsId: 'docker', usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'DOCKER_PASSWORD')]) {
                        sh """
                        echo "$DOCKER_PASSWORD" | docker login -u "$DOCKER_USERNAME" --password-stdin
                        docker push mariemkhamassi/alpine:1.0.0
                        """
                    }
                }
            }
        }

    }
}


