def call(Map config = [:]) {
    def composeFile = config.conposeFile ?: 'docker-compose.test.yaml'
    def workDir     = config.wordDir     ?: '.'

    def upCmd = "docker-compose -f ${composeFile} up --build --abort-on-container-exit"

    try {
        dir(workDir) {
            sh "docker-compose -f ${composeFile} down -v || true"

            def exitCode = sh(
                script: upCmd,
                returnStatus: true
            )

            if (exitCode != 0) {
                error "Test failed with exit code ${exitCode}"
            }
        }
    } finally {
        dir(workDir) {
            sh "docker-compose -f ${composeFile} down -v || true"
        }
    }
}