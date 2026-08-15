def call(Map config = [:]) {
    def repo          = config.repo          ?: error('cloneTerraformRepo: repo is required')
    def branch        = config.branch        ?: 'main'
    def dir           = config.dir           ?: 'terraform-infra'

    sh """
        git clone -b ${branch} https://\${GIT_USER}:\${GIT_TOKEN}@${repo.replace('https://', '')} ${dir}
        git config --global user.email "jenkins@local"
        git config --global user.name "Jenkins"
    """
}