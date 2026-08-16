def call(Map config = [:]) {
    def branch        = config.branch        ?: 'main'
    def message       = config.message       ?: "ci: update image tags"
    def dir           = config.dir           ?: 'terraform-infra'
    def file          = config.file          ?: 'terraform.image.auto.tfvars'
    def repo          = config.repo          ?: error('pushTerraformRepo: repo is required')
    def credentialsId = config.credentialsId ?: 'github-credentials'

    withCredentials([usernamePassword(
        credentialsId: credentialsId,
        usernameVariable: 'GIT_USER',
        passwordVariable: 'GIT_TOKEN'
    )]) {
        sh """
            cd ${dir}
            git add ${file}
            if ! git diff --staged --quiet; then
                git commit -m "${message}"
                git push https://\${GIT_USER}:\${GIT_TOKEN}@${repo.replace('https://', '')} ${branch}
            else
                echo "No changes to commit"
            fi
            cd ..
            rm -rf ${dir}
        """
    }
}