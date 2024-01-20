
pipeline {
   // agent any
    //agent { docker { image 'maven:3.6.3' } }
    environment {
        dockerHome = tool 'myDocker'
        mavenHome = tool 'myMaven'
        PATH = "$dockerHome/bin:$mavenHome/bib:$PATH"
    }
    stages  {
           stage('Build') {
             steps {
                sh 'mvn --version'
                echo "build"
             }
           }
           stage('Test') {
             steps {
                echo "Test"
                    }
                   }
            stage('Integration test') {
             steps {
                  echo "Integration Test"
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