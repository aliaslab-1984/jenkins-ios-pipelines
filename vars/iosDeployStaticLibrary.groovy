def call(Map config) {

  pipeline {
    def credentialsId = config.credentialsId
    def gitUri = config.gitUri
    def gitBranch = config.gitBranch ?: 'master'
    def type = config.type ?: 'universal'

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

      stage ('Build static library') {
        if (type == "device") {
            sh "bundle exec fastlane build_device_static"
        } else if (type == "universal") {
            sh "bundle exec fastlane build_universal_static"
        } else if (type == "simulator") {
            sh "bundle exec fastlane build_simulator_static"
        }
      }

      stage ('Deploy') {
        if (type == "device") {
            sh "bundle exec fastlane deploy_device_static"
        } else if (type == "universal") {
            sh "bundle exec fastlane deploy_universal_static"
        } else if (type == "simulator") {
            sh "bundle exec fastlane deploy_simulator_static"
        }
      }
    }
  }
}
