def call(Map config = [:]) {
    def message = config.message ?: 'ci: update'

    sh """
        if ! git diff --staged --quiet; then
            git commit -m "${message}"
        else
            echo "No changes to commit"
        fi
    """
}