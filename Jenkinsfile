pipeline {
    agent any
    environment {
        SONAR_HOST_URL = 'http://172.17.0.1:9000/'
        SONAR_LOGIN = credentials('Sonarqube')
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
                // Generate the Jacoco coverage report
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
            withCredentials([usernamePassword(credentialsId: 'nexus-credentials', usernameVariable: 'NEXUS_USERNAME', passwordVariable: 'NEXUS_PASSWORD')]) {
                // Obtenir l'ID du composant existant dans Nexus sans interpolation Groovy
                def component_id = sh(script: 'curl -u "${NEXUS_USERNAME}:${NEXUS_PASSWORD}" "http://localhost:8081/service/rest/v1/components?repository=maven-releases&group=tn.esprit.spring&name=kaddem&version=0.0.1" | grep -o \'"id":"[^"]*\' | sed \'s/"id":"\\(.*\\)"/\\1/\'', returnStdout: true).trim()
                
                if (component_id) {
                    echo "Deleting component with ID: ${component_id}"
                    sh 'curl -X DELETE -u "${NEXUS_USERNAME}:${NEXUS_PASSWORD}" "http://localhost:8081/service/rest/v1/components/${component_id}"'
                } else {
                    echo "No component found with version 0.0.1 to delete."
                }

                // Déploiement vers Nexus
                echo 'Deploying to Nexus'
                sh 'mvn deploy -Dnexus.username=$NEXUS_USERNAME -Dnexus.password=$NEXUS_PASSWORD'
            }
        }
    }
}

    }
}
