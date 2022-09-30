//Declarative pipeline

pipeline {
    agent any

    stages {
        stage('checkout Stage') {
            steps {
                checkout([$class: 'GitSCM', branches: [[name: '*/main']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[url: 'https://github.com/Nvsprasanna/pipeline.git']]])

            }
        }
        stage('Build stage') {
            steps {
                echo 'we are in builld statge'
            }
        }
        stage('Upload stage') {
            steps {
                echo 'we are in upload statge'
            }
        }
        stage('Artifcat check') {
            steps {
                echo 'we are in  statge'
            }
        }
    }
}
