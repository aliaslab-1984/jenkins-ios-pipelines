def call(Map config) {

  pipeline {
    def coverageFilePath = config.coverageFilePath ?: 'fastlane/cobertura_report.xml'

    iosCheckOutAndLint(config)

    ansiColor {
      stage ('UI Test') {
          sh "bundle exec fastlane sdk_ui_test"
          cobertura coberturaReportFile: coverageFilePath
      }
    }
  }
}
