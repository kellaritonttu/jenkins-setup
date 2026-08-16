def call(Map config = [:]) {
    def image         = config.image         ?: error('dockerBuild: image is required')
    def tag           = config.tag           ?: error('dockerBuild: tag is required')
    def dockerfile    = config.dockerfile    ?: 'Dockerfile'
    def credentialsId = config.credentialsId ?: 'dockerhub-credentials'

    sh "docker build -f ${dockerfile} -t ${image}:${tag} ."

    withCredentials([usernamePassword(
        credentialsId: credentialsId,
        usernameVariable: 'DOCKERHUB_USER',
        passwordVariable: 'DOCKERHUB_TOKEN'
    )]) {
        sh """
            echo \$DOCKERHUB_TOKEN | docker login -u \$DOCKERHUB_USER --password-stdin
            docker push ${image}:${tag}
        """
    }
}