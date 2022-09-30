//Declarative pipeline

pipeline {
    agent any

    stages {
        stage('checkout Stage') {
            steps {
                echo 'we are in checkout statge'
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
