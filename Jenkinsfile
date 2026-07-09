pipeline {

    agent any

    parameters {
        string(
                name: 'TAGS',
                defaultValue: '@smoke',
                description: 'Enter Cucumber Tags'
        )
    }

    stages {

        stage('Checkout') {

            steps {
                checkout scm
            }
        }

        stage('Build & Execute Tests') {

            steps {

                bat """
                mvn clean test -Dcucumber.filter.tags="${TAGS}"
                """
            }
        }
    }

    post {

        always {

            archiveArtifacts artifacts: 'test-output/**/*', allowEmptyArchive: true
        }

        success {

            echo 'Execution Successful'
        }

        failure {

            echo 'Execution Failed'
        }
    }
}