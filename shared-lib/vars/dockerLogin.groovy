import org.myjenkins.Docker

def call(Map config = [:]) {
    new Docker(this).login(config.credentialsId ?: 'dockerhub-credentials')
}