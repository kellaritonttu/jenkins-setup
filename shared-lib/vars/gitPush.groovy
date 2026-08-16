// vars/gitPush.groovy
def call(Map config = [:]) {
    def branch        = config.branch        ?: 'main'
    def repo          = config.repo          ?: error('gitPush: repo is required')
    def credentialsId = config.credentialsId ?: 'github-credentials'
    
    def repoHost      = repo.replace('https://', '')

    withCredentials([usernamePassword(
        credentialsId: credentialsId,
        usernameVariable: 'GIT_USER',
        passwordVariable: 'GIT_TOKEN'
    )]) {
        sh """
            git remote set-url origin https://\${GIT_USER}:\${GIT_TOKEN}@${repoHost}
            git push origin HEAD:${branch}
        """
    }
}