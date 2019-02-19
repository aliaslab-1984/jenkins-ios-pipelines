def call(Map config) {

  pipeline {
    def credentialsId = config.credentialsId
    def gitUri = config.gitUri
    def coverageFilePath = config.coverageFilePath

    if (gitUri == null || credentialsId == null || coverageFilePath == null) {
        throw new IllegalStateException('Missing configuration arguments')
    }

    stage ('Checkout') {
        git credentialsId: credentialsId, url: gitUri
    }

    stage ('Fetch Dependencies') {
        sh "bundle exec fastlane bootstrap"
    }

    stage ('Unit Test') {
        sh "bundle exec fastlane sdk_test"
        cobertura coberturaReportFile: coverageFilePath
    }
  }
}
