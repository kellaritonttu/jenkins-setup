def call(Map config = [:]) {
    def branch  = config.branch  ?: 'main'
    def message = config.message ?: "ci: update image tags"
    def dir     = config.dir     ?: 'terraform-infra'
    def file    = config.file    ?: 'terraform.image.auto.tfvars'
    def credentialsId = config.credentialsId ?: 'github-credentials'

    withCredentials([usernamePassword(
        credentialsId: credentialsId,
        usernameVariable: 'GIT_USER',
        passwordVariable: 'GIT_TOKEN'
    )]) {
        sh """
            cd ${dir}
            git add ${file}
            git commit -m "${message}"
            git push origin ${branch}
            cd ..
            rm -rf ${dir}
        """
    }
}