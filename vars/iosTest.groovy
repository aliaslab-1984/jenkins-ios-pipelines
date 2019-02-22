def call(Map config) {

  pipeline {
    def credentialsId = config.credentialsId
    def gitUri = config.gitUri
    def coverageFilePath = config.coverageFilePath
    def gitBranch = config.gitBranch ?: 'master'

    if (gitUri == null || credentialsId == null || coverageFilePath == null) {
        throw new IllegalStateException('Missing configuration arguments')
    }

    stage ('Checkout') {
        git credentialsId: credentialsId, url: gitUri, branch: gitBranch
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
