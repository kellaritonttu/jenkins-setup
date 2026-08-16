def call(Map config = [:]) {
    def credentialsId = config.credentialsId ?: 'dockerhub-credentials'

    withCredentials([usernamePassword(
        credentialsId: credentialsId,
        usernameVariable: 'DOCKERHUB_USER',
        passwordVariable: 'DOCKERHUB_TOKEN'
    )]) {
        sh "echo \$DOCKERHUB_TOKEN | docker login -u \$DOCKERHUB_USER --password-stdin"
    }
}