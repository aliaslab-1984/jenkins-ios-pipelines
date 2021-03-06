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
          sh "bundle exec fastlane sdk_bootstrap"
      }

      stage ('Build') {
            if (type == null || type == "device") {
                sh "bundle exec fastlane build_device_framework_alt"
                sh "bundle exec fastlane check_framework_dev_alt"
            } else if (type == "universal") {
                sh "bundle exec fastlane build_universal_framework_alt"
                sh "bundle exec fastlane check_framework_univ"
            } else if (type == "xc") {
                sh "bundle exec fastlane build_xcframework_alt"
            }
      }

      if (buildOnly) {
          return
      }

      stage ('Deploy') {
          if (type == null || type == "device") {
              sh "bundle exec fastlane deploy_device_framework_alt"
          } else if (type == "universal") {
              sh "bundle exec fastlane deploy_universal_framework_alt"
          } else if (type == "xc") {
              sh "bundle exec fastlane deploy_xcframework_alt"
          }
      }
    }
  }
}
