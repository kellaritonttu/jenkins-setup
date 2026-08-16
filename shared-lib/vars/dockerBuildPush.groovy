def call(Map config = [:]) {
    def image      = config.image      ?: error('dockerBuild: image is required')
    def tag        = config.tag        ?: error('dockerBuild: tag is required')
    def dockerfile = config.dockerfile ?: 'Dockerfile'
    def context    = config.context    ?: '.'

    sh "docker build -f ${dockerfile} -t ${image}:${tag} ${context}"
    sh "docker push ${image}:${tag}"

}