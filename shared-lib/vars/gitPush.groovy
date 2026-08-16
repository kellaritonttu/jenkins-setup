import org.myjenkins.Git

def call(Map config = [:]) {
    def branch        = config.branch        ?: 'main'
    def repo          = config.repo          ?: error('gitPush: repo is required')
    def credentialsId = config.credentialsId ?: 'github-credentials'

    withCredentials([usernamePassword(
        credentialsId: credentialsId,
        usernameVariable: 'GIT_USER',
        passwordVariable: 'GIT_TOKEN'
    )]) {
        def url = Git.authenticatedUrl(repo, env.GIT_USER, env.GIT_TOKEN)
        
        sh "git push ${url} ${branch}"
    }
}