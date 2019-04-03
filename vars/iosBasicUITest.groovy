def call(Map config) {

  pipeline {
    def coverageFilePath = config.coverageFilePath ?: 'fastlane/cobertura_report.xml'

    iosCheckOutAndLint(config)

    ansiColor {
      stage ('Basic UI Test') {
          sh "bundle exec fastlane sdk_basic_ui_test"
          cobertura coberturaReportFile: coverageFilePath
      }
    }
  }
}
