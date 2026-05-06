pipeline {
    agent any

    environment {
        DOCKERHUB_CREDENTIALS = credentials('docker-cred')
        IMAGE_NAME = 'techsubrat07/devsecops-bankapp'
        IMAGE_TAG = "${BUILD_NUMBER}"
        REMOTE_HOST = 'ec2-user@35.171.82.156'
        REMOTE_APP_NAME = 'jenkins_dockerapp'
    }

    stages {

        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/subratgithub/DevSecOps-BankingApp.git'
            }
        }

       stage('Build + Test + Coverage + Sonar') {
    steps {
        withSonarQubeEnv('sonar-server') {
            sh '''
            mvn clean verify sonar:sonar \
            -Dsonar.junit.reportPaths=target/surefire-reports \
            -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml \
            -Dsonar.ws.timeout=120
            '''
        }
    }
}

        stage('Quality Gate') {
            steps {
                timeout(time: 2, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }

        stage('Verify Artifacts') {
            steps {
                sh 'ls -la target'
                sh 'ls -la target/site/jacoco'
            }
        }

        

        stage('Build Docker Image') {
            steps {
                sh "docker build -t $IMAGE_NAME:$IMAGE_TAG ."
            }
        }

        stage('Cleanup Old Files') {
    steps {
        sh '''
            docker system prune -af || true
            docker volume prune -f || true
            rm -rf /var/lib/jenkins/.cache/trivy || true
        '''
    }
}

        stage('Trivy Scan') {
    steps {
        sh """
            trivy image \
            --scanners vuln \
            --severity HIGH,CRITICAL \
            --exit-code 1 \
            --no-progress \
            $IMAGE_NAME:$IMAGE_TAG
        """
    }
}

        stage('Push to Docker Hub') {
            steps {
                sh """
                    echo $DOCKERHUB_CREDENTIALS_PSW | docker login -u $DOCKERHUB_CREDENTIALS_USR --password-stdin
                    docker push $IMAGE_NAME:$IMAGE_TAG
                """
            }
        }

        stage('Deploy to EC2') {
            steps {
                sshagent(['ssh-acceskey']) {
                    sh """
                        ssh -o StrictHostKeyChecking=no $REMOTE_HOST '
                            docker pull $IMAGE_NAME:$IMAGE_TAG &&
                            docker stop $REMOTE_APP_NAME || true &&
                            docker rm $REMOTE_APP_NAME || true &&
                            docker run -d \
                                --restart unless-stopped \
                                --name $REMOTE_APP_NAME \
                                -p 9090:9090 \
                                $IMAGE_NAME:$IMAGE_TAG &&
                            docker image prune -f
                        '
                    """
                }
            }
        }

        
    }

    post {
        always {
            junit 'target/surefire-reports/*.xml'

            publishHTML([
                allowMissing: false,
                alwaysLinkToLastBuild: true,
                keepAll: true,
                reportDir: 'target/site/jacoco',
                reportFiles: 'index.html',
                reportName: 'JaCoCo Coverage Report'
            ])
        }

        success {
            echo '✅ Deployment Successful!'
        }

        failure {
            echo '❌ Deployment Failed!'
        }
    }
}
