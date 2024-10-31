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
            // Install jq temporarily if not already available
           // sh 'which jq || apt-get update && apt-get install -y jq'

            // Use jq to get the component ID
            sh 'curl -u "admin:nexus" "http://localhost:8081/service/rest/v1/components?repository=maven-releases&group=tn.esprit.spring&name=kaddem&version=0.0.1" | | jq -r .items[].id)'
            sh '''
component_id=$(curl -u admin:root "http://localhost:8081/service/rest/v1/components?repository=maven-releases&group=tn.esprit.spring&name=kaddem&version=0.0.1" | jq -r .items[].id)
echo "Deleting component with ID: $component_id"
curl -X DELETE -u admin:root "http://localhost:8081/service/rest/v1/components/$component_id"'''
            // Deploy to Nexus
            echo 'Deploying to Nexus'
            sh 'mvn deploy -Dnexus.username=admin -Dnexus.password=nexus'
        }
    }
}


    }
}
