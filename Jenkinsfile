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
                withCredentials([usernamePassword(credentialsId: 'nexusCredentials', usernameVariable: 'NEXUS_USERNAME', passwordVariable: 'NEXUS_PASSWORD')]) {
                    sh 'mvn clean deploy -DskipTests -DaltDeploymentRepository=nexus::default::${NEXUS_URL}/repository/${NEXUS_REPOSITORY}'
                }
            }
        }
    }
}
