def call(Map config) {

  pipeline {
    def coverageFilePath = config.coverageFilePath

    iosCheckOutAndLint(config)

    ansiColor {
      stage ('Unit Test') {
          sh "bundle exec fastlane sdk_test"
          cobertura coberturaReportFile: coverageFilePath
      }
    }
  }
}
