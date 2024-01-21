
pipeline {
      agent any
    //agent { docker { image 'maven:3.6.3' } }
    environment {
        dockerHome = tool 'myDocker'
        mavenHome = tool 'myMaven'
        PATH = "$dockerHome/bin:$mavenHome/bin:$PATH"
        ARG JENKINS_HOME_USER_ID
        ARG JENKINS_HOME_GROUP_ID

        RUN groupadd -g $JENKINS_HOME_GROUP_ID jenkins && \
            useradd -m jenkins -u $JENKINS_HOME_USER_ID -g $JENKINS_HOME_GROUP_ID
    }
    stages  {
           stage('Checkout') {
             steps {
                sh 'mvn --version'
                sh 'docker version'
                echo 'sudo Build'
                echo "PATH - $PATH "
                echo "Build Number - $env.BUILD_Number"
                echo "Build ID - $env.BUILD_ID"
                echo "Job Name - $env.JOB_NAME"
                echo "Job ID - $env.JOB_ID"
                echo "Build TAG - $env.BUILD_TAG"
                echo "Build URL - $env.BUILD_URL"
             }
           }
           stage('Compile') {
                        steps {
                          sh "mvn clean compile"
                        }
                      }

           stage('Test') {
             steps {
                echo "Test"
                 sh "mvn test"
                    }
                   }
            stage('Integration test') {
            steps {
                    echo "Integration Test"
                    sh "mvn failsafe:integration-test failsafe:verify"
                    }
                   }
    }
    post {
            always  {
                 echo "I am awsome , I run always"
            }
            success {
                 echo "I run when you are successful"

            }
            failure {
                echo "I run when you fail"
            }
            }

}