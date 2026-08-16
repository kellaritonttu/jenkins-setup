def call(Map config = [:]) {
    def message = config.message ?: 'ci: update'

    sh [
        'bash', '-c', '''
            if ! git diff --staged --quiet; then
                git commit -m "$1"
            else
                echo "No changes to commit"
            fi
        ''', message
    ]
}