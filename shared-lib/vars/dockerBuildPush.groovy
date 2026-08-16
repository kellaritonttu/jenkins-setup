import org.myjenkins.Docker

def call(Map config = [:]) {
    def image      = config.image      ?: error('dockerBuildAndPush: image is required')
    def tag        = config.tag        ?: error('dockerBuildAndPush: tag is required')
    def dockerfile = config.dockerfile ?: 'Dockerfile'
    def context    = config.context    ?: '.'

    def docker = new Docker(this)

    docker.build(
        image: image, 
        tag: tag,
        dockerfile: dockerfile, 
        context: context
    )
    docker.push(
        image: image, 
        tag: tag,
    )
}