import org.myjenkins.Git

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
        def url = Git.authenticatedUrl(repo, env.GIT_USER, env.GIT_TOKEN)
        
        sh """
            cd ${dir}
            git add ${file}
            if ! git diff --staged --quiet; then
                git commit -m "${message}"
                git push ${url} ${branch}
            else
                echo "No changes to commit"
            fi
            cd ..
            rm -rf ${dir}
        """
    }
}