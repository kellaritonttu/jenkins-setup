def call(Map config = [:]) {
    def branch    = config.branch    ?: 'main'
    def changelog = config.changelog ?: false

    checkout(
        scm: scm,
        branch: branch,
        changelog: changelog    
    )
}