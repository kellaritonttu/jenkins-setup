def call(Map config = [:]) {
    def image = config.image ?: error('dockerBuildAndPush: image is required')

    sh "docker push ${image}:latest"
}