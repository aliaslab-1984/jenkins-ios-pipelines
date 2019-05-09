def call(Map config) {

  pipeline {
    def coverageFilePath = config.coverageFilePath ?: 'fastlane/cobertura_report.xml'

    iosCheckOutAndLint(config)

    ansiColor {
      stage ('All Test') {
          sh "bundle exec fastlane sdk_all_test"
          cobertura coberturaReportFile: coverageFilePath
      }
    }
  }
}
