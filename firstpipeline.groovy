//Declarative pipeline

pipeline {
    agent any
    parameters {  string(name: 'MYJOB_NAME', defaultValue: 'firstpipeline', description: '')
                  string(name: 'MYSOURCE_BRANCH', defaultValue: 'master', description: '')
                  string(name: 'MYBUILD_NUMBER', defaultValue: '10', description: '')
                  choices(name: 'ip', choices: ['QA,3.85.91.150','Dev,3.95.229.196', 'Prod,18.234.225.172'], description: 'select IP Value') 
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
        stage('Deployment step') {
            steps {
                echo 'we are in  deploymnet step'
                sh ''' echo "$MYJOB_NAME, $MYSOURCE_BRANCH, $MYBUILD_NUMBER"
                IFS=',' read -ra myArray <<< "$myip"
                for ip in "${myArray[@]}"
                do
                    if [ $ip == ${myArray[0]} ]; then 
                        echo "You first value is not IP ignored it"
                    else
                aws s3 cp s3://nvsbucket/$MYJOB_NAME/$MYSOURCE_BRANCH/$MYBUILD_NUMBER/hello-$MYBUILD_NUMBER.war .
                scp -o StrictHostKeyChecking=no -i /tmp/Linuxserver.pem hello-$MYBUILD_NUMBER.war ec2-user@$ip:/var/lib/tomcat/webapps
                fi
                done
                '''
            }
        }
    }
}
