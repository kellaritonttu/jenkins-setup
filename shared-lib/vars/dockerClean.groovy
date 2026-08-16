def call(Map config = [:]) {
    def image = config.image ?: error('dockerClean: image is required')

    sh """
            docker rmi ${image} || true
        """
}