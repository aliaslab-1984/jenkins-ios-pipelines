def call(Map config) {

  pipeline {
    def credentialsId = config.credentialsId
    def gitUri = config.gitUri
    def gitBranch = config.gitBranch ?: 'master'
    def type = config.type
    def buildOnly = config.buildOnly

    if (gitUri == null || credentialsId == null) {
        throw new IllegalStateException('Missing configuration arguments')
    }

    stage ('Checkout') {
        git credentialsId: credentialsId, url: gitUri, branch: gitBranch
    }

    ansiColor {
      stage ('Fetch Dependencies') {
          sh "bundle exec fastlane ios sdk_bootstrap"
      }

      stage ('Build') {
            if (type == null || type == "device") {
                sh "bundle exec fastlane ios build_check_device_framework"
            } else if (type == "universal") {
                sh "bundle exec fastlane ios build_check_universal_framework"
            } else if (type == "xc") {
                sh "bundle exec fastlane ios build_check_xc_framework"
            }
      }

      if (buildOnly) {
          return
      }

      stage ('Deploy') {
          if (type == null || type == "device") {
              sh "bundle exec fastlane ios deploy_device_framework"
          } else if (type == "universal") {
              sh "bundle exec fastlane ios deploy_universal_framework"
          } else if (type == "xc") {
              sh "bundle exec fastlane ios deploy_xcframework"
          }
      }
    }
  }
}
