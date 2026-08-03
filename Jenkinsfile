pipeline {
    agent any

    tools {
        // This name must match the name configured in Jenkins -> Global Tool Configuration -> NodeJS
        nodejs 'NodeJS-18.x'
    }

    stages {
        stage('Install Dependencies') {
            steps {
                sh 'npm install'
            }
        }

        stage('Build') {
            steps {
                sh 'npm run build'
            }
        }

        stage('Test') {
            steps {
                sh 'npm run test -- --no-watch --code-coverage --browsers=ChromeHeadless'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                // This name must match the SonarQube server configuration name in Jenkins
                withSonarQubeEnv('sonar') {
                    // Execute SonarScanner
                    sh 'npx sonar-scanner'
                }
            }
        }
    }
}
