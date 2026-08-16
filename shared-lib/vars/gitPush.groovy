def call(Map config = [:]) {
    def branch        = config.branch        ?: 'main'
    def credentialsId = config.credentialsId ?: 'github-credentials'

    if (!branch.matches(/^[a-zA-Z0-9_\-.\/]+$/)) {
        error("gitPush: Invalid branch name '${branch}'")
    }

    withCredentials([usernamePassword(
        credentialsId: credentialsId,
        usernameVariable: 'GIT_USER',
        passwordVariable: 'GIT_TOKEN'
    )]) {
 
        sh ["git", "push", "origin", "HEAD:${branch}"]
 
    }
}