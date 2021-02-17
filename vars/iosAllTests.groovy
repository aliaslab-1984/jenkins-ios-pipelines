def call(Map config) {

  pipeline {
    def coverageFilePath = config.coverageFilePath ?: 'fastlane/cobertura_report.xml'

    iosCheckOutAndLint(config)

    ansiColor {
      stage ('All Test') {
          sh "bundle exec fastlane sdk_all_test"
      }

      stage ('Coverage') {
          cobertura coberturaReportFile: coverageFilePath
      }
      
      stage ('Sonar') {
          sh "bundle exec fastlane sonar_analysis"
      }
    }
  }
}
