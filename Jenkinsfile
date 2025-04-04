pipeline {
    agent any

    tools {
        maven 'Maven'
        jdk 'JDK21'
    }

    environment {
        WORKSPACE_DIR = "${pwd()}" // Store workspace directory dynamically
    }

    stages {
        stage('Checkout jm-common') {
            steps {
                git(
                    url: 'https://github.com/punjabskillsbank/jm-common.git',
                    branch: 'develop'
                )
            }
        }

        stage('Build jm-common') {
            steps {
                dir("${WORKSPACE_DIR}/jm-common") {  // Navigate dynamically
                    sh 'mvn clean install -DskipTests'  // Install jm-common in local Maven repo
                }
            }
        }

        stage('Checkout jm-profile-management') {
            steps {
                git(
                    url: 'https://github.com/punjabskillsbank/jm-profile-management.git',
                    branch: 'develop'
                )
            }
        }

        stage('Build jm-profile-management') {
            steps {
                dir("${WORKSPACE_DIR}/jm-profile-management") {  // Navigate dynamically
                    sh 'mvn clean install -DskipTests'  // Build profile-management project
                }
            }
        }

        stage('Run tests') {
            steps {
                dir("${WORKSPACE_DIR}/jm-profile-management") {
                    sh 'mvn test'
                }
            }
        }
    }

    post {
        always {
            junit '**/target/surefire-reports/*.xml'  // Publish test results
        }
        failure {
            echo "Tests failed! Check the report."
        }
    }
}
