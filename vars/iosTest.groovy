def call(Map config) {

  pipeline {
    def coverageFilePath = config.coverageFilePath ?: 'fastlane/cobertura_report.xml'
    
    sh "echo before the checkout and lint"

    iosCheckOutAndLint(config)
    
    sh "echo after the checkout and lint"

    ansiColor {
      
      sh "echo before the unnit test"
      
      stage ('Unit Test') {
          sh "bundle exec fastlane sdk_test"
      }
      
      sh "echo after the unnit test"
      
      stage ('Coverage') {
          cobertura coberturaReportFile: coverageFilePath
      }
    }
  }
}
