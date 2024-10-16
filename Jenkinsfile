
pipeline {
    agent any
    
    stages {
        stage('Checkout from Git') {
            steps {
                echo 'Pulling from Git'
                git branch: 'mustaphaa', url: 'https://github.com/ismailgharnougui/Devops'
            }
        }
        
        stage('Testing Maven') {
            steps {
                sh 'mvn -version'
            }
        }

        stage('Deploy to Nexus') {
            steps {
                echo 'Deploying to Nexus...'
                withCredentials([usernamePassword(credentialsId: NEXUS_CREDENTIALS_ID, usernameVariable: 'admin', passwordVariable: 'mustapha')]) {
                    sh """
                        ${MAVEN_HOME}/bin/mvn deploy \
                        -DskipTests \
                        -DaltDeploymentRepository=nexus::default::${NEXUS_URL} \
                        -Dnexus.username=${NEXUS_USERNAME} \
                        -Dnexus.password=${NEXUS_PASSWORD}
                    """
                }
            }
        } // Close the Deploy to Nexus stage
    } // Close the stages block
}
