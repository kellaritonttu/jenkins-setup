def call(Map config = [:]) {
    def email = config.email ?: 'jenkins@local'
    def name  = config.name  ?: 'Jenkins'

    sh [
        "git", "config", "user.email", email,
        "git", "config", "user.name", name
    ]
}