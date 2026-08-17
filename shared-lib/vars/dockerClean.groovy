def call(Map config = [:]) {
    def images = config.images ?: error('dockerClean: images is required')

    def imageList = images instanceof List ? images : [images]
    
    imageList.each { image ->
        sh "docker rmi ${image} || true"
    }
}