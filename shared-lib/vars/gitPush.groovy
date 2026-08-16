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
        sh """
            REPO_PATH=\$(git remote get-url origin | sed 's|https://github.com/||')
            git push https://\${GIT_USER}:\${GIT_TOKEN}@github.com/\${REPO_PATH} HEAD:${branch}
        """
    }
}