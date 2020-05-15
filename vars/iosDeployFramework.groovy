def call(Map config) {

  pipeline {
    def credentialsId = config.credentialsId
    def gitUri = config.gitUri
    def gitBranch = config.gitBranch ?: 'master'
    def project_directory = config.sdk_project_directory
    def type = config.type

    if (gitUri == null || credentialsId == null) {
        throw new IllegalStateException('Missing configuration arguments')
    }

    stage ('Checkout') {
        git credentialsId: credentialsId, url: gitUri, branch: gitBranch
    }

    ansiColor {
      stage ('Fetch Dependencies') {
          sh "bundle exec fastlane sdk_bootstrap"
      }

      stage ('Build') {
            if (type == null || type == "device") {
                sh "bundle exec fastlane build_device_framework"
            } else if (type == "universal") {
                sh "bundle exec fastlane build_universal_framework"
            } else if (type == "xc") {
                sh "bundle exec fastlane build_xcframework"
            }
      }

      stage ('Deploy') {
          if (type == null || type == "device") {
              sh "bundle exec fastlane deploy_device_framework"
          } else if (type == "universal") {
              sh "bundle exec fastlane deploy_universal_framework"
          } else if (type == "xc") {
              sh "bundle exec fastlane deploy_xcframework"
          }
      }
    }
  }
}
