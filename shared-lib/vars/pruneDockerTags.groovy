import org.myjenkins.DockerHub

def call(Map config = [:]) {
    def username      = config.username      ?: error('pruneDockerTags: username is required')
    def repo          = config.repo          ?: error('pruneDockerTags: repo is required')
    def keepLast      = config.keepLast      ?: 3
    def credentialsId = config.credentialsId ?: 'dockerhub-credentials'
    def protectedTags = config.protectedTags ?: ['latest']

    def apiToken = ''

    // login
    withCredentials([usernamePassword(
        credentialsId: credentialsId,
        usernameVariable: 'DOCKERHUB_USER',
        passwordVariable: 'DOCKERHUB_TOKEN'
    )]) {
        def response = sh(
            script: '''
                curl -fsSL -X POST https://hub.docker.com/v2/users/login/ \
                    -H 'Content-Type: application/json' \
                    -d '{"username":"'"$DOCKERHUB_USER"'","password":"'"$DOCKERHUB_TOKEN"'"}'
            ''',
            returnStdout: true
        ).trim()

        apiToken = DockerHub.parseToken(response)
    }

    // get tags
    def response = sh(
        script: """
            curl -fsSL \
                "https://hub.docker.com/v2/repositories/${username}/${repo}/tags/?page_size=100&ordering=-last_updated" \
                -H "Authorization: JWT ${apiToken}"
        """,
        returnStdout: true
    ).trim()

    def tags = DockerHub.parseTags(response)
    echo "Found ${tags.size()} tag(s), keeping ${keepLast} most recent"

    if (tags.size() <= keepLast) {
        echo "Nothing to delete"
        return
    }

    def tagsToDelete = tags[keepLast..-1]

    for (def tag in tagsToDelete) {
        if (protectedTags.contains(tag.name)) {
            echo "Skipping protected tag: ${tag.name}"
            continue
        }
        echo "Deleting: ${tag.name}"
        sh """
            curl -fsSL -X DELETE \
                "https://hub.docker.com/v2/repositories/${username}/${repo}/tags/${tag.name}/" \
                -H "Authorization: JWT ${apiToken}"
        """
    }
}