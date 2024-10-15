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
                echo 'Checking Maven version'
                sh 'mvn -version'
            }
        }

        stage('Deploy to Nexus') {
            steps {
                echo 'Deploying to Nexus...'
                // Exécuter la commande mvn deploy et ignorer les tests
                // Le dépôt cible "deploymentRepo" doit correspondre à l'id du repository dans le pom.xml
                withCredentials([usernamePassword(credentialsId: 'nexus-credentials', 
                                                 usernameVariable: 'admin', 
                                                 passwordVariable: 'mustapha')]) {
                    sh """
                        mvn deploy -DskipTests \
                            -DaltDeploymentRepository=deploymentRepo::default::http://localhost:8081/repository/maven-releases/ \
                            -Dnexus.username=$USERNAME \
                            -Dnexus.password=$PASSWORD
                    """
                }
            }
        } // Fin du stage Deploy to Nexus
    } // Fin du block stages
}
