def call(Map config) {

  pipeline {
    def credentialsId = config.credentialsId
    def gitUri = config.gitUri
    def gitBranch = config.gitBranch ?: 'master'
    def lintReportPattern = config.lintReportPattern ?: '**/swiftlint-results.xml'

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

      stage('Lint') {
          sh "bundle exec fastlane sdk_lint"
          recordIssues tool: swiftLint(pattern: lintReportPattern)
      }
    }
  }
}
