def call(Map config) {

  pipeline {
    def credentialsId = config.credentialsId
    def gitUri = config.gitUri
    def artifactoryPropertiesFile = config.artifactoryPropertiesFile
    def gitBranch = config.gitBranch ?: 'master'
    def project_directory = config.sdk_project_directory

    if (gitUri == null || credentialsId == null || artifactoryPropertiesFile == null) {
        throw new IllegalStateException('Missing configuration arguments')
    }

    stage ('Checkout') {
        git credentialsId: credentialsId, url: gitUri, branch: gitBranch
    }

    ansiColor {
      stage ('Fetch Dependencies') {
          sh "bundle exec fastlane sdk_bootstrap"
      }

      stage ('Deploy') {
        if (project_directory == null) {
          sh "bundle exec fastlane build_deploy_framework config_name:${artifactoryPropertiesFile}"
        } else {
          sh "bundle exec fastlane build_deploy_framework config_name:${artifactoryPropertiesFile} sdk_project_directory:${project_directory}"
        }
      }
    }
  }
}
