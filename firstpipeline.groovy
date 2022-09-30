//Declarative pipeline

pipeline {
    agent any

    stages {
        stage('checkout Stage') {
            steps {
                checkout([$class: 'GitSCM', branches: [[name: '*/main']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[url: 'https://github.com/Nvsprasanna/Sep22-code.git']]])
                sh '''ls -la'''

            }
        }
        stage('Build stage') {
            steps {
                echo 'we are in builld statge'
                sh ''' mvn clean package 
                ls -la target/ '''

            }
        }
        stage('Upload stage') {
            steps {
                echo 'we are in upload statge'
                sh ''' aws s3 cp target/hello-*.war s3://nvsbucket/$JOB_NAME/BRANCH/$BUILD_NUMBER/ '''
            }
        }
        stage('Artifcat check') {
            steps {
                echo 'we are in  statge'
                sh ''' aws s3 ls '''
            }
        }
    }
}
