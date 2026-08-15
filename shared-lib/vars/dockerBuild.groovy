import org.myjenkins.DockerHub

def call(Map confit = [:]) {
    def image         = config.image         ?: error('dockerBuild: image is required')
    def tag           = config.tag           ?: error('dockerBuild: tag is required')
    def dockerfile    = config.dockerfile    ?: 'Dockerfile'
    def credentialsId = config.credentialsId ?: 'dockerhub-credentials'

    sh "docker build -f ${dockerfile} -t ${image}:${tag} ."

    def hub = new DockerHub(this)
    hub.login(credentialsId)
    hub.push(image, tag)
}