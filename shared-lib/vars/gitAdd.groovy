def call(Map config = [:]) {
    def file = config.file ?: error('gitAdd: file is required')

    sh "git add ${file}"
}