def call(Map config) {

  pipeline {
    def coverageFilePath = config.coverageFilePath
    
	checkOutAndLint(config)

    stage ('Basic UI Test') {
        sh "bundle exec fastlane sdk_test"
        cobertura coberturaReportFile: coverageFilePath
    }
  }
}
