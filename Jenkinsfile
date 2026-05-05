pipeline {
    agent any

    options {
        timestamps()
        timeout(time: 1, unit: 'HOURS')
        buildDiscarder(logRotator(numToKeepStr: '10'))
    }

    environment {
        APP_NAME = "bankapp"
        JAR_FILE = "target/bankapp-0.0.1-SNAPSHOT.jar"
        EC2_USER = "ec2-user"  // Amazon Linux user
        EC2_HOST = credentials('EC2_HOST')  // Store EC2 instance IP or DNS
        EC2_KEY = credentials('EC2_KEY')    // Store path to EC2 key pair
        EC2_DEPLOY_PATH = "/home/${EC2_USER}/bankapp"
        SERVICE_NAME = "bankapp"
        PORT = "8080"
    }

    stages {
        stage('Checkout') {
            steps {
                echo "Checking out source code..."
                checkout scm
            }
        }

        stage('Build') {
            steps {
                echo "Building application with Maven..."
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Test') {
            steps {
                echo "Running unit tests..."
                sh 'mvn test'
            }
        }

        stage('Code Coverage') {
            steps {
                echo "Generating code coverage report..."
                sh 'mvn jacoco:report'
                publishHTML([
                    reportDir: 'target/site/jacoco',
                    reportFiles: 'index.html',
                    reportName: 'JaCoCo Coverage Report'
                ])
            }
        }

        stage('Archive Artifacts') {
            steps {
                echo "Archiving build artifacts..."
                archiveArtifacts artifacts: '${JAR_FILE}', fingerprint: true
            }
        }

        stage('Deploy to Development') {
            when {
                branch 'develop'
            }
            steps {
                echo "Deploying to Amazon EC2 development environment..."
                sh '''
                    ssh -i ${EC2_KEY} -o StrictHostKeyChecking=no ${EC2_USER}@${EC2_HOST} "mkdir -p ${EC2_DEPLOY_PATH}"
                    scp -i ${EC2_KEY} -o StrictHostKeyChecking=no ${JAR_FILE} ${EC2_USER}@${EC2_HOST}:${EC2_DEPLOY_PATH}/app.jar
                    ssh -i ${EC2_KEY} -o StrictHostKeyChecking=no ${EC2_USER}@${EC2_HOST} "sudo systemctl stop ${SERVICE_NAME} || true"
                    ssh -i ${EC2_KEY} -o StrictHostKeyChecking=no ${EC2_USER}@${EC2_HOST} "cd ${EC2_DEPLOY_PATH} && sudo systemctl start ${SERVICE_NAME}"
                '''
            }
        }

        stage('Deploy to Production') {
            when {
                branch 'main'
            }
            steps {
                echo "Deploying to Amazon EC2 production environment..."
                sh '''
                    ssh -i ${EC2_KEY} -o StrictHostKeyChecking=no ${EC2_USER}@${EC2_HOST} "mkdir -p ${EC2_DEPLOY_PATH}"
                    scp -i ${EC2_KEY} -o StrictHostKeyChecking=no ${JAR_FILE} ${EC2_USER}@${EC2_HOST}:${EC2_DEPLOY_PATH}/app.jar
                    ssh -i ${EC2_KEY} -o StrictHostKeyChecking=no ${EC2_USER}@${EC2_HOST} "sudo systemctl stop ${SERVICE_NAME} || true"
                    ssh -i ${EC2_KEY} -o StrictHostKeyChecking=no ${EC2_USER}@${EC2_HOST} "cd ${EC2_DEPLOY_PATH} && sudo systemctl start ${SERVICE_NAME}"
                '''
            }
        }

        stage('Health Check') {
            steps {
                echo "Running health checks on Amazon EC2..."
                sh '''
                    sleep 15
                    ssh -i ${EC2_KEY} -o StrictHostKeyChecking=no ${EC2_USER}@${EC2_HOST} "curl -f http://localhost:${PORT}/login || exit 1"
                '''
            }
        }
    }

    post {
        always {
            echo "Pipeline execution completed."
        }

        success {
            echo "✅ Pipeline executed successfully!"
            echo "Application deployed to EC2 at http://${EC2_HOST}:${PORT}"
        }

        failure {
            echo "❌ Pipeline failed. Check logs for details."
        }

        unstable {
            echo "⚠️ Pipeline unstable. Review test results."
        }
    }
}

