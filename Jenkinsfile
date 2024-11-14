pipeline {
    agent any

    stages {
        stage('Checkout from Git') {
            steps {
                echo 'Pulling from Git'
                git branch: 'Ismail', url: 'https://github.com/ismailgharnougui/Devops'
            }
        }

        stage('Maven Clean, Compile, Install, and Package') {
            steps {
                echo 'Running Maven Clean, Compile, Install, and Package'
                sh 'mvn clean install package'
            }
        }

        stage('Tests - JUnit/Mockito') {
            steps {
                echo 'Running Tests'
                sh 'mvn test'
            }
        }

        stage('Generate JaCoCo Report') {
            steps {
                echo 'Generating JaCoCo Report'
                // Le rapport JaCoCo est maintenant généré automatiquement pendant la phase de test
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
                withCredentials([string(credentialsId: 'sonar-login', variable: 'SONAR_LOGIN'),
                                 string(credentialsId: 'sonar-password', variable: 'SONAR_PASSWORD')]) {
                    sh """
                        mvn sonar:sonar \
                            -Dsonar.host.url=http://192.168.0.10:9000 \
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
        }
    }
}
