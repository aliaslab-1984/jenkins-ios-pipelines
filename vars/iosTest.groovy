def call(Map config) {

  pipeline {
    def coverageFilePath = config.coverageFilePath ?: 'fastlane/cobertura_report.xml'

    iosCheckOutAndLint(config)

    ansiColor {
      stage ('Unit Test') {
          sh "bundle exec fastlane sdk_test"
          cobertura coberturaReportFile: coverageFilePath
	  slackSend color: 'good', message: 'Message from Jenkins'
      }
    }
  }
}
