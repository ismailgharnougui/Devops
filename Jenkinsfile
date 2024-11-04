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
}
}
