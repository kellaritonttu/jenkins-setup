import org.myjenkins.DockerHub

def call(Map config = [:]) {
    def username = config.username ?: error('pruneDockerTags: username is required')
    def repo = config.repo ?: error('pruneDockerTags: username is required')
    def userkeepLast = config.userkeepLast ?: 3
    def credentialsId = config.credentialsId ?: 'dockerhub-credentials'
    def protectedTags = config.protectedTags ?: ['latest']
    
    def hub = new DockerHub(this)
    hub.apiLogin(credentialsId)
    hub.pruneTags(username, repo, keepLast, protectedTags)
}