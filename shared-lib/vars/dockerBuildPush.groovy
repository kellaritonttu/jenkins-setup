def call(Map config = [:]) {
    def image      = config.image      ?: error('dockerBuildAndPush: image is required')
    def tag        = config.tag        ?: error('dockerBuildAndPush: tag is required')
    def dockerfile = config.dockerfile ?: 'Dockerfile'
    def context    = config.context    ?: '.'

    sh "docker build -f ${config.dockerfile} -t ${config.image}:${config.tag} ${config.context}"
    sh "docker push ${image}:${tag}"
}