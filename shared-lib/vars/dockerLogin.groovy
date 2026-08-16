import org.myjenkins.Docker

def call(Map config = [:]) {
    def credentialsId = config.credentialsId ?: 'dockerhub-credentials'

    withCredentials([usernamePassword(
        credentialsId: credentialsId,
        usernameVariable: 'DOCKER_USER',
        passwordVariable: 'DOCKER_PASS'
    )]) {
        new Docker(this).login()
    }

}