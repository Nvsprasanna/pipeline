//Declarative pipeline

pipeline {
    agent any
    parameters {  string(name: 'MYJOB_NAME', defaultValue: 'firstpipeline', description: '')
                  string(name: 'MYSOURCE_BRANCH', defaultValue: 'master', description: '')
             }
    
    stages {
        stage('checkout Stage') {

            steps {
            checkout([$class: 'GitSCM', branches: [[name: '*/$MYSOURCE_BRANCH']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[credentialsId: 'jenkins-user-github',url: 'https://github.com/Nvsprasanna/myprivaterepo.git']]])
                sh '''ls -la'''
                echo " We are connected in private source code"

            }
        }
        
        
    }
}