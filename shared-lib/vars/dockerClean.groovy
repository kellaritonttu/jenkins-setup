import org.myjenkins.Docker

def call(Map config = [:]) {
    def images = config.images ?: error('dockerClean: images is required')

    new Docker(this).clean(images)
}