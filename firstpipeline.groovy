//Declarative pipeline

pipeline {
    agent any
    parameters { 
                  string(name: 'MYSOURCE_BRANCH', defaultValue: 'master', description: '')
                  choice(name: 'myip', choices: ['QA,172.31.92.239','Dev,172.31.88.212', 'Prod,172.31.80.36'], description: 'select IP Value') 
               }
    
    stages {
        stage('checkout Stage') {

            steps {
            checkout([$class: 'GitSCM', branches: [[name: '*/$MYSOURCE_BRANCH']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[url: 'https://github.com/Nvsprasanna/Sep22-code.git']]])
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
                sh ''' aws s3 cp target/hello-*.war s3://nvsbucket/$JOB_NAME/$SOURCE_BRANCH/$BUILD_NUMBER/ '''
            }
        }
        stage('Artifcat check') {
            steps {
                echo 'we are in  statge'
                sh ''' aws s3 ls s3://nvsbucket/$JOB_NAME/$SOURCE_BRANCH/$BUILD_NUMBER/ '''
            }
        }
        
    }
}
