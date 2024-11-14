pipeline {
    agent any

    tools {
        maven 'M2_HOME'
    }

    environment {
        SONAR_HOST_URL = 'http://192.168.0.10:9000'
        SONAR_LOGIN = 'admin'
        SONAR_PASSWORD = 'Gharnougui123@'
        NEXUS_URL = 'http://192.168.0.10:8081' // Updated Nexus URL to match the pom.xml
        NEXUS_REPOSITORY = 'maven-releases'
        NEXUS_GROUP = 'tn.esprit.spring'
        NEXUS_ARTIFACT = 'kaddem'
        NEXUS_VERSION = '0.0.1'
    }
   stages {
        stage('Check MySQL Status') {
            steps {
                script {
                    echo 'Checking MySQL Service Status'
                    def mysqlStatus = sh(script: 'systemctl is-active mysql || echo "inactive"', returnStdout: true).trim()
                    if (mysqlStatus == 'inactive') {
                        error("MySQL service is not active. Please start MySQL before running this pipeline.")
                    } else {
                        echo "MySQL service is running."
                    }
                }
            }
        }
    stages {
        stage('Checkout from Git') {
            steps {
                echo 'Pulling from Git'
                git branch: 'Ismail', url: 'https://github.com/ismailgharnougui/Devops'
            }
        }

        stage('Maven Clean Compile') {
            steps {
                echo 'Running Maven Clean and Compile'
                sh 'mvn clean compile'
            }
        }

        stage('Maven Install') {
            steps {
                echo 'Running Maven Install'
                sh 'mvn install'
            }
        }

        stage('Build Package') {
            steps {
                echo 'Running Maven Package'
                sh 'mvn package'
            }
        }

        stage('Tests - JUnit/Mockito') {
            steps {
                echo 'Running Tests'
                sh 'mvn test'
            }
        }

        stage('JaCoCo Coverage Report') {
            steps {
                echo 'Publishing JaCoCo Coverage Report'
                jacoco execPattern: '**/target/jacoco.exec',
                       classPattern: '**/classes',
                       sourcePattern: '**/src',
                       exclusionPattern: '/target/**,**/*Test,**/*_javassist/**'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                sh """
                    mvn sonar:sonar \
                        -Dsonar.host.url=${SONAR_HOST_URL} \
                        -Dsonar.login=${SONAR_LOGIN} \
                        -Dsonar.password=${SONAR_PASSWORD} \
                        -Dsonar.exclusions="src/main/java/tn/esprit/spring/kaddem/entities/Equipe.java,\
                        src/main/java/tn/esprit/spring/kaddem/entities/DetailEquipe.java,\
                        src/main/java/tn/esprit/spring/kaddem/entities/Etudiant.java,\
                        src/main/java/tn/esprit/spring/kaddem/entities/Departement.java,\
                        src/main/java/tn/esprit/spring/kaddem/controllers/DepartementRestController.java,\
                        src/main/java/tn/esprit/spring/kaddem/services/DepartementServiceImpl.java,\
                        src/main/java/tn/esprit/spring/kaddem/controllers/EquipeRestController.java,\
                        src/main/java/tn/esprit/spring/kaddem/services/EquipeServiceImpl.java,\
                        src/main/java/tn/esprit/spring/kaddem/controllers/EtudiantRestController.java,\
                        src/main/java/tn/esprit/spring/kaddem/services/EtudiantServiceImpl.java,\
                        src/main/java/tn/esprit/spring/kaddem/KaddemApplication.java,\
                        src/main/java/tn/esprit/spring/kaddem/entities/Niveau.java,\
                        src/main/java/tn/esprit/spring/kaddem/entities/Option.java,\
                        src/main/java/tn/esprit/spring/kaddem/entities/Universite.java,\
                        src/main/java/tn/esprit/spring/kaddem/controllers/UniversiteRestController.java,\
                        src/main/java/tn/esprit/spring/kaddem/services/UniversiteServiceImpl.java"
                """
            }
        }

         stage('Deploy to Nexus') {
                  steps {
                      echo 'Deploying to Nexus Repository'
                      sh 'mvn clean deploy -DskipTests'
                  }
              }
      stage('Build Docker Image') {
                steps {
                    script {
                        echo 'Building Docker Image'
                        def dockerImage = docker.build("ismailgharnougui/ismail")  // Nom d'image en minuscules et caractères valides
                    }
                }
            }

            stage('Deploy Image to DockerHub') {
                steps {
                    script {
                        echo 'Logging into DockerHub and Pushing Image'
                        withCredentials([usernamePassword(credentialsId: 'Docker_credentials', passwordVariable: 'DOCKER_PASSWORD', usernameVariable: 'DOCKER_USERNAME')]) {
                            sh 'docker login -u $DOCKER_USERNAME -p $DOCKER_PASSWORD'
                            sh 'docker push ismailgharnougui/ismail'  // Nom d'image en minuscules
                        }
                    }
                }
            }

            stage('Deploy with Docker Compose') {
                steps {
                    script {
                        echo 'Deploying with Docker Compose'
                        sh 'docker-compose up -d'
                    }
                }
            }
        }

        post {
            success {
                echo 'Sending success email...'
                mail to: 'ismaelgharnougui@gmail.com',
                     subject: "Pipeline Jenkins - Success - Build #${BUILD_NUMBER}",
                     body: """<html>
                                <body>
                                    <h2 style="color: #4CAF50;">ismailgharnougui Build ${BUILD_NUMBER}</h2>
                                    <div style="border: 2px solid #4CAF50; padding: 10px;">
                                        <h3 style="background-color: #4CAF50; color: white; padding: 10px; text-align: center;">
                                            Pipeline Status: SUCCESS
                                        </h3>
                                        <p>Check the <a href="${BUILD_URL}console">console output</a> for more details.</p>
                                    </div>
                                </body>
                              </html>""",
                     mimeType: 'text/html'
            }
            failure {
                echo 'Sending failure email...'
                mail to: 'ismaelgharnougui@gmail.com',
                     subject: "Pipeline Jenkins - Failure - Build #${BUILD_NUMBER}",
                     body: """<html>
                                <body>
                                    <h2 style="color: #D32F2F;">ismailgharnougui Build ${BUILD_NUMBER}</h2>
                                    <div style="border: 2px solid #D32F2F; padding: 10px;">
                                        <h3 style="background-color: #D32F2F; color: white; padding: 10px; text-align: center;">
                                            Pipeline Status: FAILURE
                                        </h3>
                                        <p>Check the <a href="${BUILD_URL}console">console output</a> for more details.</p>
                                    </div>
                                </body>
                              </html>""",
                     mimeType: 'text/html'
            }
            always {
                echo 'Pipeline completed.'
            }
        }
    }
}
