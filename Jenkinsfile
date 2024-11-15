pipeline {
    agent any

    tools {
        maven 'M2_HOME'
    }

    environment {
        SONAR_HOST_URL = 'http://192.168.0.10:9000'
        SONAR_LOGIN = 'admin'
        SONAR_PASSWORD = 'Gharnougui123@'
        NEXUS_URL = 'http://192.168.0.10:8081'
        NEXUS_REPOSITORY = 'maven-releases'
        NEXUS_GROUP = 'tn.esprit.spring'
        NEXUS_ARTIFACT = 'kaddem'
        NEXUS_VERSION = '0.0.1'
        DOCKER_USERNAME = 'ismailgharnougui'
        DOCKER_PASSWORD = 'Gharnougui123@'

    }

    stages {
        stage('Checkout from Git') {
            steps {
                echo 'Pulling from Git'
                git branch: 'ISMAIL', url: 'https://github.com/ismailgharnougui/Devops.git'
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

        stage('Verify Target Directory') {
            steps {
                echo 'Checking for JAR file in target directory'
                sh 'ls -l target'
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
                  // Liste les fichiers dans le répertoire target pour s'assurer que le JAR est là
                  sh 'ls -l target'

                  // Vérifie si le fichier JAR existe
                  def jarExists = fileExists 'target/kaddem-0.0.1.jar'

                  if (jarExists) {
                      echo 'Building Docker Image'
                      sh 'docker build -t ismailgharnougui/ismailgharnougui:0.0.1 .'
                  } else {
                      error 'JAR file not found in target directory. Aborting Docker build.'
                  }
              }
          }
      }


        stage('Deploy Image to DockerHub') {
            steps {
                echo 'Logging into DockerHub and Pushing Image'
                sh "echo ${DOCKER_PASSWORD} | docker login -u ${DOCKER_USERNAME} --password-stdin"
                sh 'docker push ismailgharnougui/ismailgharnougui:0.0.1'
            }
        }

        stage('Deploy with Docker Compose') {
            steps {
                echo 'Deploying with Docker Compose'
                sh 'docker-compose up -d'
            }
        }
    }

    post {
        always {
            echo 'Pipeline completed.'
        }
        success {
            echo 'Pipeline succeeded!'
        }
        failure {
            echo 'Pipeline failed.'
        }
    }

     post {
            success {
                echo 'Sending success email...'
                mail to: 'ismail.elgharnougui@esprit.tn',
                     subject: "Pipeline Jenkins - Success - Build #${BUILD_NUMBER}",
                     body: """<html>
                                <body>
                                    <h2 style="color: #4CAF50;">ISMAIL Build ${BUILD_NUMBER}</h2>
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
                mail to: 'ismail.elgharnougui@esprit.tn',
                     subject: "Pipeline Jenkins - Failure - Build #${BUILD_NUMBER}",
                     body: """<html>
                                <body>
                                    <h2 style="color: #D32F2F;">ISMAIL Build ${BUILD_NUMBER}</h2>
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
