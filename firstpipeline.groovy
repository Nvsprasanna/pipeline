//Declarative pipeline

pipeline {
    agent any
    parameters {  string(name: 'MYJOB_NAME', defaultValue: 'firstpipeline', description: '')
                  string(name: 'MYSOURCE_BRANCH', defaultValue: 'master', description: '')
                  string(name: 'MYBUILD_NUMBER', defaultValue: '10', description: '') 
               }
    
    stages {
        stage('checkout Stage') {
            steps {
            checkout([$class: 'GitSCM', branches: [[name: '*/$SOURCE_BRANCH']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[url: 'https://github.com/Nvsprasanna/Sep22-code.git']]])
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
        stage('Deployment step') {
            steps {
                echo 'we are in  deploymnet step'
                sh ''' echo "$MYJOB_NAME, $MYSOURCE_BRANCH, $MYBUILD_NUMBER"
                '''
            }
        }
    }
}
