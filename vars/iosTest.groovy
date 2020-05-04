def call(Map config) {

  pipeline {
    def coverageFilePath = config.coverageFilePath ?: 'fastlane/cobertura_report.xml'

    iosCheckOutAndLint(config)

    ansiColor {
      stage ('Unit Test') {
          sh "bundle exec fastlane sdk_test"
      }
      
      stage ('Coverage') {
          cobertura coberturaReportFile: coverageFilePath
      }
    }
  }
}
