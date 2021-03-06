def call(Map config) {

  pipeline {
    def coverageFilePath = config.coverageFilePath ?: 'fastlane/cobertura_report.xml'
    
    echo 'before the checkout and lint'

    iosCheckOutAndLint(config)
    
    echo 'after the checkout and lint'

    ansiColor {
      
      echo 'before the unit test'
      
      stage ('Unit Test') {
          sh "bundle exec fastlane sdk_test"
      }
      
      echo 'after the unit test'
      
      stage ('Coverage') {
          cobertura coberturaReportFile: coverageFilePath
      }
      
      stage ('Sonar') {
          sh "bundle exec fastlane sonar_analysis"
      }
    }
  }
}
