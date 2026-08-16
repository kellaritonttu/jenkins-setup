import org.myjenkins.Docker

def call(Map config = [:]) {
    def credentialsId = config.credentialsId ?: 'dockerhub-credentials'

    def docker = new Docker(this)

    withCredentials([usernamePassword(
        credentialsId: credentialsId,
        usernameVariable: 'DOCKER_USER',
        passwordVariable: 'DOCKER_PASS'
    )]) {
        docker.login()
    }

}