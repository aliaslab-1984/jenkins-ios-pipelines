def call(Closure closure) {
   try {
     slackSend color: '#ffff00', message: "${env.JOB_NAME} build ${env.BUILD_NUMBER} started";
     closure();
     slackSend color: 'good', message: "${env.JOB_NAME} build ${env.BUILD_NUMBER} succeded";
   }
   catch (Exception | AssertionError exc) {
     slackSend color: '#ff0000', message: "${env.JOB_NAME} build ${env.BUILD_NUMBER} failed";
   }
}
