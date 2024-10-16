pipeline {
    agent any
    
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

        stage('SonarQube Analysis') {
            steps {
                echo 'Running SonarQube Analysis'
                // Define the Maven tool
                def mvn = tool 'Default Maven'  // Ensure this matches the name of your Maven tool in Jenkins
                // Perform SonarQube analysis
                withSonarQubeEnv('SonarQube') {  // Ensure 'SonarQube' matches your Jenkins SonarQube instance
                    sh "${mvn}/bin/mvn clean verify sonar:sonar \
                        -Dsonar.projectKey=Devops \
                        -Dsonar.projectName='Devops' \
                        -Dsonar.branch.name=Mariem \
                        -Dsonar.host.url=http://192.168.230.140:9000/tutorials?id=Devops\
                        -Dsonar.login=Mariem-roken"
                }
            }
        }

        stage('JUnit/Mockito Tests') {
            steps {
                echo 'Running JUnit/Mockito Tests'
                sh 'mvn test'
            }
        }

        stage('Deploy to Nexus') {
            steps {
                echo 'Deploying to Nexus...'
                sh 'mvn deploy -DskipTests -X'
            }
        }
    }
}
