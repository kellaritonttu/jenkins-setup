def call(Map config = [:]) {
    def file          = config.file          ?: 'terraform/terraform.image.auto.tfvars'
    def key           = config.key           ?: error('updateImageTag: key is required')
    def tag           = config.tag           ?: error('updateImageTag: backendTag is required')

    sh """
        # update backend image tag
        sed -i 's/${key}[[:space:]]*=.*/${key} = \"${tag}\"/' ${file}
    """
}