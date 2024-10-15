pipeline {
    agent any
    
    stages {
        stage('Checkout from Git') {
            steps {
                echo 'Pulling from Git'
                git branch: 'Mariem', url: 'https://github.com/ismailgharnougui/Devops'
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
                // Run mvn deploy and skip tests
                sh 'mvn deploy -DskipTests -x'
            }
        } 
    } 
}
