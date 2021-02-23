def call(Map config) {

  pipeline {
    def credentialsId = config.credentialsId
    def gitUri = config.gitUri
    def gitBranch = config.gitBranch ?: 'master'
    def lintReportPattern = config.lintReportPattern ?: '**/swiftlint-results.xml'
    def coverageFilePath = config.coverageFilePath ?: 'fastlane/cobertura_report.xml'

    if (gitUri == null || credentialsId == null) {
        throw new IllegalStateException('Missing configuration arguments')
    }

    stage ('Checkout') {
        git credentialsId: credentialsId, url: gitUri, branch: gitBranch
    }

    ansiColor {
      stage ('Fetch Dependencies') {
          sh "bundle exec fastlane app_bootstrap"
      }

      stage ('All Tests') {
          sh "bundle exec fastlane app_test"
      }

      stage ('Sonar') {
          sh "bundle exec fastlane sonar_analysis"
      }

      stage ('Coverage') {
          cobertura coberturaReportFile: coverageFilePath
      }

    }
  }
}
