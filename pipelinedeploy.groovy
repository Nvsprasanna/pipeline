//Declarative pipeline

pipeline {
    agent any
    parameters {  string(name: 'MYJOB_NAME', defaultValue: 'firstpipeline', description: '')
                  string(name: 'MYSOURCE_BRANCH', defaultValue: 'master', description: '')
                  string(name: 'MYBUILD_NUMBER', defaultValue: '10', description: '')
                  choice(name: 'myip', choices: ['QA,172.31.92.239','Dev,172.31.88.212', 'Prod,172.31.80.36'], description: 'select IP Value') 
               }
    
    stages {

         stage('Ask Deploy'){
            input{
                message "do you want to proceed with deploymnet"
                }
                steps {
                    sh 'echo "Deploying to server"'
                }
         }
         stage('Artifcat check') {
            steps {
                echo 'we are in artifact statge'
                sh ''' aws s3 ls s3://nvsbucket/$MYJOB_NAME/$MYSOURCE_BRANCH/$MYBUILD_NUMBER/ '''
                echo 'artificat change finished'
                echo '#?'
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
