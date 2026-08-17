def call(Map config = [:]) {
    def credentialsId = config.credentialsId ?: 'dockerhub-credentials'

    withCredentials([usernamePassword(
        credentialsId: credentialsId,
        usernameVariable: 'DOCKER_USER',
        passwordVariable: 'DOCKER_PASS'
    )]) {
        sh "echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin"
    }
}