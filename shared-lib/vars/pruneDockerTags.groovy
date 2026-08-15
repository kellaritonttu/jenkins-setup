import org.myjenkins.DockerHub
import groovy.json.JsonSlurperClassic

def call(Map config = [:]) {
    def username      = config.username      ?: error('pruneDockerTags: username is required')
    def repo          = config.repo          ?: error('pruneDockerTags: repo is required')
    def keepLast      = config.keepLast      ?: 3
    def credentialsId = config.credentialsId ?: 'dockerhub-credentials'
    def protectedTags = config.protectedTags ?: ['latest']

    def apiToken = ''

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

        apiToken = new JsonSlurperClassic().parseText(response).token
    }

    def tagsResponse = sh(
        script: """
            curl -fsSL \
                "https://hub.docker.com/v2/repositories/${username}/${repo}/tags/?page_size=100&ordering=-last_updated" \
                -H "Authorization: JWT ${apiToken}"
        """,
        returnStdout: true
    ).trim()

    def tags = new JsonSlurperClassic()
        .parseText(tagsResponse)
        .results
        .collect { [name: it.name, last_updated: it.last_updated] }

    echo "Found ${tags.size()} tag(s), keeping ${keepLast} most recent"

    if (tags.size() <= keepLast) {
        echo "Nothing to delete"
        return
    }

    def tagsToDelete = tags[keepLast..-1]

    for (int i = 0; i < tagsToDelete.size(); i++) {
        def tag = tagsToDelete[i]
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